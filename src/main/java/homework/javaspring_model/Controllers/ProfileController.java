package homework.javaspring_model.Controllers;

import homework.javaspring_model.Config.DatabaseInitializer;
import homework.javaspring_model.Models.User.Role;
import homework.javaspring_model.Models.User.User;
import homework.javaspring_model.Models.User.UserDto;
import homework.javaspring_model.Services.RoleService;
import homework.javaspring_model.Services.UserService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;


@Controller
@AllArgsConstructor
public class ProfileController {
    private final RoleService roleService;
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    @GetMapping("/login")
    public String getLoginScreen() {
        return "loginPage";
    }
    //Не нужен, разлогинивание происходит через Spring Security
    @GetMapping("/logout")
    public String Logout() {
        return "redirect:/";
    }
    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        try {
            var currentUser = userService.findByUsername(userDetails.getUsername());
            currentUser.ifPresentOrElse(p->{
                model.addAttribute("user", p);
                if(Objects.equals(p.getRole().getName(), "COMPANY"))
                    model.addAttribute("isAddProductFormActive", true);
            }, ()->{
                model.addAttribute("message", "Данные пользователя не найдены");
                    });
            return "profile";
        } catch (Exception e) {
            model.addAttribute("message", "Непредвиденная ошибка!");
            log.debug("Метод: getProfile; Сообщение: {}", e.getMessage());
            return "redirect:/";
        }
    }

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/adminPanel")
    public String getAdminPanel(@AuthenticationPrincipal UserDetails userDetails) {

        //Из-за Spring Security досюда даже не дойдёт
        if(!userService.isUserHasRole(userDetails.getUsername(), "ADMIN"))
            return "redirect:/";

        return "adminPanel";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User()); // Используем DTO
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserDto userDto,
                               RedirectAttributes redirectAttributes) {
        try {
            // Проверяем существование пользователя
            if (userService.findByUsername(userDto.getUsername()).isPresent()) {
                redirectAttributes.addFlashAttribute("error",
                        "Пользователь с таким логином уже существует");
                return "redirect:/register";
            }

            // Создаем пользователя
            User user = new User(userDto);

            // Получаем или создаем роль USER
            Role userRole = roleService.findByName("USER")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("USER");
                        return roleService.addRole(role).orElseThrow();
                    });

            user.setRole(userRole);

            userService.addUser(user);

            redirectAttributes.addFlashAttribute("success",
                    "Регистрация успешна! Теперь вы можете войти.");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Ошибка при регистрации: " + e.getMessage());
            return "redirect:/register";
        }
    }
}

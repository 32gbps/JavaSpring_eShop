package homework.javaspring_model.Controllers;

import homework.javaspring_model.Models.Role;
import homework.javaspring_model.Models.User;
import homework.javaspring_model.Repositories.RoleRepository;
import homework.javaspring_model.Repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public RegistrationController(UserRepository userRepository,
                                  RoleRepository roleRepository,
                                  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User()); // Используем DTO
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
                               RedirectAttributes redirectAttributes) {
        try {
            // Проверяем существование пользователя
            if (userRepository.findUserByUsername(user.getUsername()).isPresent()) {
                redirectAttributes.addFlashAttribute("error",
                        "Пользователь с таким логином уже существует");
                return "redirect:/register";
            }

            // Создаем пользователя
//            User user = new User();
//            user.setUsername(dto.getUsername());
//            user.setPassword(passwordEncoder.encode(dto.getPassword()));
//            user.setEmail(dto.getEmail());
//            user.setName(dto.getName());
//            user.setSurname(dto.getSurname());
//            user.setEnabled(true);

            // Получаем или создаем роль USER
            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("USER");
                        return roleRepository.save(role);
                    });

            user.setRoles(Set.of(userRole));

            userRepository.save(user);

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
package homework.javaspring_model.Controllers;

import homework.javaspring_model.Models.Clothes;
import homework.javaspring_model.Models.ClothesType;
import homework.javaspring_model.Models.Role;
import homework.javaspring_model.Models.User;
import homework.javaspring_model.Repositories.RoleRepository;
import homework.javaspring_model.Repositories.UserRepository;
import homework.javaspring_model.Services.ClothesService;
import homework.javaspring_model.Services.UserDetailsServiceImpl;
import jakarta.annotation.security.RolesAllowed;
import net.datafaker.Faker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Random;
import java.util.Set;

@Controller
public class ProfileController {
    private final ClothesService clothesService;
    private final Faker faker;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public ProfileController(ClothesService clothesService,
                             UserDetailsServiceImpl userService,
                             UserRepository userRepository,
                             RoleRepository roleRepository,
                             PasswordEncoder passwordEncoder
                             ) {
        faker = new Faker();
        this.clothesService = clothesService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

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
    public String getProfile(Model model) {
        model.addAttribute("title", "Личный кабинет");
        model.addAttribute("user", new User());
        return "profile";
    }
    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/adminPanel")
    public String getAdminPanel(Model model) {
        return "adminPanel";
    }
    @GetMapping("/fillBaseRandObjects{count}")
    public String fillBase(int count) {
        Random r = new Random();
        String[] size = new String[]{"XS", "S", "M", "L", "XL", "XXL"};
        int i = 0;
        int added = 0;
        while(i < count) {
            var c = new Clothes(
                    faker.commerce().productName(),
                    ClothesType.values()[r.nextInt(ClothesType.values().length)].getDisplayName(),
                    size[r.nextInt(size.length)],
                    faker.color().name(),
                    faker.commerce().brand(),
                    Double.parseDouble(faker.commerce().price(10.0, 3000.0).replace(',','.')));

            if(!clothesService.isExistByName(c.getName()))
            {
                clothesService.addClothes(c);
                ++added;
            }

            ++i;
        }
        IO.println("Added objects into db: " + added);
        return "redirect:/profile/adminPanel";
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

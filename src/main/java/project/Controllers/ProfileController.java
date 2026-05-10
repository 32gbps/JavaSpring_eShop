package project.Controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import project.Models.User.Customer.Customer;
import project.Models.User.Customer.CustomerDto;
import project.Models.User.Role;
import project.Models.User.User;
import project.Models.User.Vendor.Vendor;
import project.Models.User.Vendor.VendorDto;
import project.Services.CustomerService;
import project.Services.RoleService;
import project.Services.UserService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.Services.VendorService;

import java.util.Locale;
import java.util.Objects;


@Controller
@AllArgsConstructor
public class ProfileController {
    private final RoleService roleService;
    private final UserService userService;
    private final CustomerService customerService;
    private final VendorService vendorService;

    private final PasswordEncoder passwordEncoder;
    private MessageSource messageSource;

    @GetMapping("/login")
    public String getLoginScreen() {
        return "loginPage";
    }
    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        try {
            var currentUser = userService.findByUsername(userDetails.getUsername());
            currentUser.ifPresentOrElse(p->{
                model.addAttribute("user", p);
                model.addAttribute("buttonSet", p.getRole().getName());
            }, ()-> model.addAttribute("message", "Данные пользователя не найдены"));
            return "profile";
        } catch (Exception e) {
            model.addAttribute("message", "Непредвиденная ошибка!");
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
    @Transactional
    @PostMapping("/register")
    public String registerUser(@ModelAttribute CustomerDto customerDto, RedirectAttributes redirectAttributes) {
        try {
            // Проверяем существование пользователя
            if (userService.findByUsername(customerDto.username()).isPresent()) {
                redirectAttributes.addFlashAttribute("error",
                        "Пользователь с таким логином уже существует");
                return "redirect:/register";
            }

            // Создаем пользователя
            User user = new User();
            user.setEmail(customerDto.email());
            user.setUsername(customerDto.username());
            user.setPassword(passwordEncoder.encode(customerDto.password()));
            // Получаем или создаем роль USER
            Role userRole = roleService.findByName("CUSTOMER")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("CUSTOMER");
                        return roleService.addRole(role).orElseThrow();
                    });

            user.setRole(userRole);

            userService.addUser(user).orElseThrow();

            Customer customer = new Customer();
            customer.setUser(user);
            customer.setName(customerDto.name());
            customer.setSurname(customerDto.surname());
            customer.setBirthDate(customerDto.birthDate());

            customerService.addCustomer(customer).orElseThrow();

            redirectAttributes.addFlashAttribute("success",
                    "Регистрация успешна! Теперь вы можете войти.");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Ошибка при регистрации: " + e.getMessage());
            return "redirect:/register";
        }
    }
    @GetMapping("registerVendor")
    public String getRegisterVendorPage() {return "registerPageVendor";}
    @PostMapping("registerVendor")
    public String postRegisterVendorPage(@ModelAttribute VendorDto dto, RedirectAttributes redirectAttributes) {
        try{
            userService.findByUsername(dto.username()).ifPresent( user-> {
                throw new RuntimeException("User already exist");
            });
            vendorService.findByName(dto.vendorName()).ifPresent( user-> {
                throw new RuntimeException("Vendor already exist");
            });

            User user = new User();
            user.setUsername(dto.username());
            user.setEmail(dto.email());
            user.setPassword(passwordEncoder.encode(dto.password()));
            user.setRole(roleService.findByName("VENDOR").orElseThrow());
            var addedUser = userService.addUser(user).orElseThrow();

            Vendor vendor = new Vendor();
            vendor.setVendorName(dto.vendorName());
            vendor.setIdentifier(dto.identifier());
            vendor.setUser(addedUser);

            vendorService.addVendor(vendor).orElseThrow();

            return "redirect:/";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при регистрации: " + e.getMessage());
            return "redirect:/registerVendor";
        }
    }
    @GetMapping("/profile/wishlist")
    public String getWishlist(Model model, Locale locale) {
        try {
            model.addAttribute("pageTitle", messageSource.getMessage("page.title.wishlist", null, locale));
            model.addAttribute("initMethod", 1);
            model.addAttribute("message", null);

            return "index";
        } catch (Exception e) {
            model.addAttribute("message", "Внутренняя ошибка при запросе товара");
            return "redirect:/";
        }
    }
    @PostMapping(value = "/profile/changePassword")
    public String changePassword(@RequestParam String oldPass, @RequestParam String newPass, Model model, @AuthenticationPrincipal UserDetails userDetails){
        try {
            String message;
            var user = userService.findByUsername(userDetails.getUsername());
            if(user.isEmpty())
                message = "Пользователь не найден";
            else{
                var userData = user.get();
                if(!Objects.equals(userData.getPassword(), oldPass))
                    message = "Некорректные данные";
                else {
                    userData.setPassword(passwordEncoder.encode(newPass));
                    userService.update(userData);
                    message = "Пароль изменён!";
                }
            }

            model.addAttribute("message", message);
            return "profile";
        }
        catch (Exception e) {
            model.addAttribute("message", "Непредвиденная ошибка!");
            return "redirect:/";
        }
    }
}

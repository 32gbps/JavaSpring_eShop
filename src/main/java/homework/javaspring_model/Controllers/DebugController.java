package homework.javaspring_model.Controllers;

import homework.javaspring_model.Models.User;
import homework.javaspring_model.Services.UserDetailsServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DebugController {

    private final UserDetailsServiceImpl userService;
    public DebugController(UserDetailsServiceImpl userService){
        this.userService = userService;
    }

    @GetMapping("/debug/auth")
    public String debugAuth(Authentication authentication) {
        if (authentication == null) {
            return "Пользователь не аутентифицирован";
        }

        return String.format(
                "Аутентифицирован: %s, Роли: %s, Привилегии: %s",
                authentication.getName(),
                authentication.getAuthorities(),
                authentication.getCredentials()
        );
    }

    @GetMapping("/debug/users")
    public List<User> debugUsers() {
        // Вернуть всех пользователей из БД
        return  userService.getAllUsers();
    }
}
package project.Controllers;

import project.Models.User.User;
import project.Services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DebugController {

    private final UserService userService;
    public DebugController(UserService userService){
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
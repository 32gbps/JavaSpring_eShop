package homework.javaspring_model.Services;

import homework.javaspring_model.Models.User;
import homework.javaspring_model.Repositories.UserRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<User> getAllUsers(){
        return userRepository.getUsersByIdGreaterThan(0L);
    }
    public Boolean isUserHasRole(String username, String role){
        var user = userRepository.findUserByUsername(username);
        if(user.isEmpty())
            return false;
        var userObj = user.get();
        return userObj.getRoles().stream()
                .anyMatch(r -> r.getName().equals(role));
    }
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        System.out.println("=== DEBUG AUTH ===");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Password: " + user.getPassword());
        System.out.println("Roles: " + user.getRoles());
        System.out.println("==================");


        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().replace("ROLE_", "")) // Убираем ROLE_ префикс
                        .toArray(String[]::new))
                        .build();
    }

}
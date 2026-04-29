package project.Services;

import project.Models.User.User;
import project.Models.User.UserDto;
import project.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public Boolean isUserHasRole(String username, String role){
        var user = findByUsername(username);
        if(user.isEmpty())
            return false;
        var userObj = user.get();
        return userObj.getRole().getName().equals(role);
    }
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        System.out.println("=== DEBUG AUTH ===");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Password: " + user.getPassword());
        System.out.println("Role: " + user.getRole());
        System.out.println("==================");


        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().getName()).build();
    }
    public Optional<User> addUser(User user){
        try{
            return Optional.of(userRepository.save(user));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    public Long getCount(){
        return userRepository.count();
    }
    public void ClearTable(){
        userRepository.deleteAll();
    }

    public void update(User user){
        userRepository.save(user);
    }
}
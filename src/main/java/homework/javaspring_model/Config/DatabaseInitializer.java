package homework.javaspring_model.Config;

import homework.javaspring_model.Models.Role;
import homework.javaspring_model.Models.User;
import homework.javaspring_model.Repositories.RoleRepository;
import homework.javaspring_model.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;           // @Component
import org.slf4j.Logger;                                  // Logger
import org.slf4j.LoggerFactory;                           // LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder; // PasswordEncoder

// User

@Component
public class DatabaseInitializer {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initTestUsers() {

        log.info("Добавление пользователей");
        // Администратор
        addUserWithRole("admin", "admin123", "ADMIN", "admin@example.com");

        // Обычный пользователь
        addUserWithRole("user", "user123", "USER", "user@example.com");
    }

    private void addUserWithRole(String username,
                         String rawPassword,
                         String role,
                         String email) {
        User currentUser;
        Role currentUserRole;
        //Проверить существование пользователя
        var userEntity = userRepository.findUserByUsername(username);
        //Если нет - добавляем
        if(userEntity.isEmpty()){
            log.info("Пользователь {} не найден в базе", username);
            currentUser = new User();
            currentUser.setUsername(username);
            currentUser.setPassword(passwordEncoder.encode(rawPassword));
            currentUser.setEmail(email);
            log.info("Создание новой учётной записи с именем {} и ролью {}", username, role);
        }
        //Если есть - тянем из базы
        else{
            currentUser = userEntity.get();
            log.info("Пользователь {} уже присутствует в базе", username);
        }

        //Проверить роль
        var roleEntity = roleRepository.findByName(role);
        //Если такой роли ещё нет - добавляем
        if(roleEntity.isEmpty())
        {
            log.info("Роль {} не найдена в базе", role);
            currentUserRole = new Role();
            currentUserRole.setName(role);
            roleRepository.save(currentUserRole);
            log.info("Роль {} добавлена в базу", role);
        }
        else{
            currentUserRole = roleEntity.get();
            log.info("Роль {} уже присутствует в базе", currentUserRole.getName());
        }


        if(!currentUser.getRoles().contains(currentUserRole))
        {
            log.info("У пользователя {} отсутствует роль {}. Добавление роли.",username, role);
            var setRoles = currentUser.getRoles();
            setRoles.add(currentUserRole);
            currentUser.setRoles(setRoles);
            log.info("Роль добавлена");
        }

        userRepository.save(currentUser);
        log.info("Данные базы обновлены. Пользователь {}, с ролью {}", username, role);
    }
}
package homework.javaspring_model.Config;

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
        // Проверяем наличие пользователей
        if (userRepository.count() == 0) {
            log.info("База данных пуста. Создаем тестовых пользователей...");

            // Администратор
            createUser("admin", "admin123", "ADMIN", "admin@example.com");

            // Обычный пользователь
            createUser("user", "user123", "USER", "user@example.com");

            log.info("Тестовые пользователи созданы: admin/admin123, user/user123");
        } else {
            log.info("База данных уже содержит {} пользователей", userRepository.count());

            // Дополнительно создаем админа, если его нет
            if (userRepository.findUserByUsername("admin").isEmpty()) {
                createUser("admin", "admin123", "ADMIN", "admin@example.com");
                log.info("Администратор создан (отсутствовал в БД)");
            }
        }
    }

    private void createUser(String username, String rawPassword,
                            String role, String email) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setRoles(roleRepository.findRoleByName(role));
            user.setEmail(email);

            userRepository.save(user);
            log.debug("Пользователь {} успешно создан", username);
        } catch (Exception e) {
            log.error("Ошибка при создании пользователя {}: {}", username, e.getMessage());
        }
    }
}
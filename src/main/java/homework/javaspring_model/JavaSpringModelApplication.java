package homework.javaspring_model;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "homework.javaspring_model.Repositories")
public class JavaSpringModelApplication {
    public static void main(String[] args) {
        SpringApplication.run(JavaSpringModelApplication.class, args);
    }
}

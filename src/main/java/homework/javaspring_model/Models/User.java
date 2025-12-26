package homework.javaspring_model.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Поле не может быть пустым")
    private String name;

    @Column(name = "surname", nullable = false)
    @NotBlank(message = "Поле не может быть пустым")
    private String surname;

    @Column(name = "email")
    @NotBlank(message = "Поле не может быть пустым")
    private String email;
}

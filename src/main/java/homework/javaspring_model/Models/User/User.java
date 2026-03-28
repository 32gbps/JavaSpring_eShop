package homework.javaspring_model.Models.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    public User(UserDto userDto){
        this.username = userDto.getUsername();
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "username", unique = true, nullable = false)
    @NotBlank(message = "Поле не может быть пустым")
    private String username;

    @Column(name = "password",nullable = false)
    @NotBlank(message = "Поле не может быть пустым")
    private String password;

    private boolean enabled = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roles_id")
    private Role role;

    @Override
    public String toString(){
        return String.format("Type: User\n" +
                            "Name: '%1$s'\n" +
                            "Role: '%2$s'\n", this.getUsername(), this.getRole());
    }
}

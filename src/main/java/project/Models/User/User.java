package project.Models.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "email")
    private String email;

    @Column(name = "username", unique = true, nullable = false)
    @NotBlank(message = "Поле не может быть пустым")
    private String username;

    @Column(name = "password",nullable = false)
    @NotBlank(message = "Поле не может быть пустым")
    private String password;

    @Column(name = "isEnabled",nullable = false)
    private boolean isEnabled = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roles_id")
    private Role role;

    @Override
    public String toString(){
//        String roleName = "*None*";
//        if(role != null)
//            roleName = role.getName();
        return String.format("""
                Type: User\s
                Name: %s
                Role: %s
                """, this.getUsername(), "none");
    }

    public UserDto ToDTO(){
        return new UserDto(id, username,email,password);
    }
}

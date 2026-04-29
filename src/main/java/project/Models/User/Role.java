package project.Models.User;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    public Role(String name) {
        this.name = name;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // ROLE_USER, ROLE_ADMIN

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<User> users;

    @Override
    public String toString(){
        return String.format("Rolename: %s", this.getName());
    }
}
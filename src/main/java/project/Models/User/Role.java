package project.Models.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // ROLE_USER, ROLE_ADMIN

    public Role(String roleName){
        this.name = roleName;
    }
    @Override
    public String toString(){
        return String.format("RoleName: %s", this.getName());
    }
}
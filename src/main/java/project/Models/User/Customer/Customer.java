package project.Models.User.Customer;

import lombok.Getter;
import lombok.Setter;
import project.Models.User.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID customerId;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birthdate")
    private LocalDate birthDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    public CustomerDto ToDTO(){
        try{
            var u = getUser();
            return new CustomerDto(getCustomerId(), getName(), getSurname(), getBirthDate(), u.getUsername(), u.getEmail(), u.getPassword());
        } catch (Exception e) {
            return null;
        }
    }
}

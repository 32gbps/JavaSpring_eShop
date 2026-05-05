package project.Models.User.Person;

import lombok.Getter;
import lombok.Setter;
import project.Models.Product.Product;
import project.Models.User.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birthdate")
    private LocalDate BirthDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "customer_products",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>(); //wishlist
}

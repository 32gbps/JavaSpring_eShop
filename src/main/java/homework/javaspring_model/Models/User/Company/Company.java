package homework.javaspring_model.Models.User.Company;

import homework.javaspring_model.Models.Product.Product;
import homework.javaspring_model.Models.User.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "companies")
public class Company{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "identifier")
    private String identifier;  //"xxxx-xxxx-xxxx-xxxx"

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Product> products;
}

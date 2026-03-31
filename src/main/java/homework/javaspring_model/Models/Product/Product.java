package homework.javaspring_model.Models.Product;

import homework.javaspring_model.Models.User.Company.Company;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    public Product(ProductDto dto){
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.price = dto.getPrice();
        this.company = dto.getCompany();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank(message = "Поле не может быть пустым")
    public String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "companies_id")
    private Company company;
}

package project.Models.Product;


import project.Models.User.Vendor.Vendor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.type.SqlTypes;

@Data
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
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
    @JoinColumn(name = "vendors_id")
    private Vendor vendor;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, String> attributes = new HashMap<>();
}

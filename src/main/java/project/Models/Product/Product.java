package project.Models.Product;

import lombok.Getter;
import lombok.Setter;
import project.Models.User.Vendor.Vendor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.hibernate.type.SqlTypes;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank(message = "Поле не может быть пустым")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendors_vendorId")
    private Vendor vendor;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, String> attributes = new HashMap<>();
}

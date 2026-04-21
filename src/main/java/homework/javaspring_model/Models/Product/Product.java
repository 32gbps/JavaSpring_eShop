package homework.javaspring_model.Models.Product;


import homework.javaspring_model.Models.User.Company.Company;
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
    @JoinColumn(name = "companies_id")
    private Company company;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, String> attributes = new HashMap<>();
//
//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
//    private List<ProductAttributeValue> attributes = new ArrayList<>();
//
//    // Вспомогательные методы
//    public void addAttribute(Attribute attr, String value) {
//        ProductAttributeValue pav = new ProductAttributeValue();
//        pav.setProduct(this);
//        pav.setAttribute(attr);
//        pav.setValue(value);
//        attributes.add(pav);
//    }
//
//    public Object getAttributeValue(String attrName) {
//        return attributes.stream()
//                .filter(attr -> attr.getAttribute().getName().equals(attrName))
//                .findFirst()
//                .map(ProductAttributeValue::getValue)
//                .orElse(null);
//    }
}

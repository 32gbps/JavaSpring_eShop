package homework.javaspring_model.Models.Product;

import homework.javaspring_model.Models.User.Company.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Dictionary;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    public ProductDto(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
    }

    private Long id;
    private String name;
    private String description;
    private Integer price;
    private Company company;
    private Dictionary<String, String> parameters;
}

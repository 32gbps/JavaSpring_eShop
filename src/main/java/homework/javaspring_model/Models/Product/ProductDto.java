package homework.javaspring_model.Models.Product;

import homework.javaspring_model.Models.User.Company.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private Company company;
    //private Map<String, String> attributes;
}

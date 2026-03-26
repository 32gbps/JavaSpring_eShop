package homework.javaspring_model.Models.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Dictionary;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    public ProductDto(ProductEntity entity){}

    private Long id;
    private String name;
    private String description;
    private Integer price;
    private Dictionary<String, String> parameters;
}

package homework.javaspring_model.Models.Product;

import org.springframework.stereotype.Component;

@Component
public class ProductMapper{
    public static Product DtoToEntity(ProductDto dto){
        Product p = new Product();

        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setCompany(dto.getCompany());
        p.setPrice(dto.getPrice());
        p.setAttributes(dto.getAttributes());

        return p;
    }
    public static ProductDto EntityToDto(Product entity){
        var Dto = new ProductDto();
        Dto.setId(entity.getId());
        Dto.setName(entity.getName());
        //Dto.setCompany(entity.getCompany());
        Dto.setDescription(entity.getDescription());
        Dto.setPrice(entity.getPrice());
        Dto.setAttributes(entity.getAttributes());

        return Dto;
    }
}

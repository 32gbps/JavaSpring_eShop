package project.Models.Product;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductMapper{
    public static Product DtoToEntity(ProductDto dto){
        Product p = new Product();

        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        //p.setVendor(dto.getVendor());
        p.setPrice(dto.getPrice());
        Map<String, String> attrSet = new HashMap<>();
        dto.getAttributes().forEach(attr->{
            attrSet.put(attr.getKey().toString(), attr.getValue().toString());
        });
        p.setAttributes(attrSet);

        return p;
    }
    public static ProductDto EntityToDto(Product entity){
        var Dto = new ProductDto();
        Dto.setId(entity.getId());
        Dto.setName(entity.getName());
        //Dto.setCompany(entity.getCompany());
        Dto.setDescription(entity.getDescription());
        Dto.setPrice(entity.getPrice());
        List<Attribute> attributes = new ArrayList<>();
        entity.getAttributes().forEach((key, value)->{
            attributes.add(new Attribute(key,value));
        });
        Dto.setAttributes(attributes);

        return Dto;
    }
}

package project.Models.Product;

import org.springframework.stereotype.Component;
import project.Models.User.Vendor.VendorDto;

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
        List<Attribute> attributes = new ArrayList<>();
        entity.getAttributes().forEach((key, value)->{
            attributes.add(new Attribute(key,value));
        });
        var vendor = entity.getVendor();
        var vUser = vendor.getUser();

        var vendorDto = new VendorDto(vendor.getId(),
                vendor.getVendorName(),
                vendor.getIdentifier(),
                vUser.getUsername(),
                vUser.getEmail(),
                vUser.getPassword());

        return new ProductDto(entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                vendorDto,
                attributes);
    }
}

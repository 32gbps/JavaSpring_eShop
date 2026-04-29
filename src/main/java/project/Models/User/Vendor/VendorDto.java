package project.Models.User.Vendor;

import lombok.AllArgsConstructor;
import lombok.Value;
import project.Models.Product.ProductDto;
import project.Models.User.UserDto;

import java.util.List;

@Value
@AllArgsConstructor
public class VendorDto {
    Long id;
    String name;
    String identifier;  //"xxxx-xxxx-xxxx-xxxx"
    UserDto user;
    List<ProductDto> products;
}

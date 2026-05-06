package project.Models.Product;

import lombok.AllArgsConstructor;
import lombok.Value;
import project.Models.User.Vendor.VendorDto;

import java.util.List;
import java.util.UUID;

@Value
@AllArgsConstructor
public class ProductDto {
    UUID id;
    String name;
    String description;
    Integer price;
    VendorDto vendor;
    List<Attribute> attributes;
}

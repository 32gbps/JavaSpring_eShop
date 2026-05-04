package project.Models.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.Models.User.Vendor.VendorDto;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private UUID id;
    private String name;
    private String description;
    private Integer price;
    private VendorDto vendor;
    private List<Attribute> attributes;
}

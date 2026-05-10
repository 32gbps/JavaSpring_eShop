package project.Models.Product;

import project.Models.User.Vendor.VendorDto;

import java.util.List;
import java.util.UUID;

public record ProductDto(UUID id,
                         String name,
                         String description,
                         Integer price,
                         VendorDto vendor,
                         List<Attribute> attributes) {
}

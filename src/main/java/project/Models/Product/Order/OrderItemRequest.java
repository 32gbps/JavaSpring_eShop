package project.Models.Product.Order;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderItemRequest {
    private UUID productId;
    private Integer quantity;
}

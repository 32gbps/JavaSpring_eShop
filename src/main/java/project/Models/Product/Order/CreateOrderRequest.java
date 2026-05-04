package project.Models.Product.Order;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateOrderRequest {
    private UUID personId;
    private OrderItemRequest[] items;
}

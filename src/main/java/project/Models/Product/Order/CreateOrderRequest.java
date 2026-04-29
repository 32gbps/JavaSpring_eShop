package project.Models.Product.Order;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private Long personId;
    private OrderItemRequest[] items;
}

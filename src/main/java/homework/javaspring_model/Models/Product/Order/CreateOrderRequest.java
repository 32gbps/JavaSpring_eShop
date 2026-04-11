package homework.javaspring_model.Models.Product.Order;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private Long personId;
    private List<OrderItemRequest> items;
}

package project.Models.Product.Order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal priceAtOrder;
    private BigDecimal subtotal;
}


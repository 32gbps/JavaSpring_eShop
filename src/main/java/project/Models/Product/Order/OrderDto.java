package project.Models.Product.Order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDto {
    private UUID id;
    private UUID customerId;
    private String customerName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal totalAmount;
    private List<OrderItemDto> items;
}

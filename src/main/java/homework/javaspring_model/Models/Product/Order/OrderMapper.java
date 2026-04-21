package homework.javaspring_model.Models.Product.Order;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDto toDto(Order order) {
        if (order == null) return null;

        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setPersonId(order.getPerson().getId());
        dto.setPersonName(order.getPerson().getName() + " " + order.getPerson().getSurname());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus().getDescription());
        dto.setTotalAmount(order.getTotalAmount());

        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream()
                    .map(this::toItemDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private OrderItemDto toItemDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setPriceAtOrder(item.getPriceAtOrder());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}

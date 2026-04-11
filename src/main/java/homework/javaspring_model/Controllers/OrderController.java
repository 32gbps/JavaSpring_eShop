package homework.javaspring_model.Controllers;

import homework.javaspring_model.Models.Product.Order.*;
import homework.javaspring_model.Services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    // Создание заказа
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderMapper.toDto(order));
    }

    // Получение заказов покупателя
    @GetMapping("/person/{personId}")
    public ResponseEntity<List<OrderDto>> getPersonOrders(@PathVariable Long personId) {
        List<Order> orders = orderService.getPersonOrders(personId);
        return ResponseEntity.ok(orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList()));
    }

    // Получение заказа с деталями
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrderWithDetails(orderId);
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    // Обновление статуса
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> updateStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        Order order = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    // Отмена заказа
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}

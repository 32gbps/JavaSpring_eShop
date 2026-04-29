package project.Controllers;

import project.Models.ApiResponse;
import project.Models.Product.Order.*;
import project.Services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    // Создание заказа
    @PostMapping("/createOrder")
    public ResponseEntity<ApiResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        try {
            Order order = orderService.createOrder(request);

            return ResponseEntity.ok(new ApiResponse("success", null, orderMapper.toDto(order)));
        } catch (Exception e) {

            ApiResponse errorResponse = new ApiResponse("error", "Ошибка: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Получение заказов покупателя
    @GetMapping("/person/{customerId}")
    public ResponseEntity<ApiResponse> getCustomerOrders(@PathVariable Long customerId) {
        try {
            List<OrderDto> orders = orderService.getCustomerOrders(customerId);

            return ResponseEntity.ok(new ApiResponse("success", null, orders));
        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse("error", "Ошибка: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
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

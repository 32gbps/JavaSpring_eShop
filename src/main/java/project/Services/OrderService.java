package project.Services;

import lombok.AllArgsConstructor;
import project.Models.Product.Order.Order;
import project.Models.Product.Order.OrderMapper;
import project.Models.Product.Product;
import project.Models.User.Customer.Customer;
import project.Models.Product.Order.*;
import project.Models.User.User;
import project.Repositories.OrderRepository;
import project.Repositories.CustomerRepository;
import project.Repositories.ProductRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.Repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private OrderMapper orderMapper;

    // Создание заказа
    public Order createOrder(CreateOrderRequest request) {
        // Находим покупателя
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Person not found"));

        // Создаем заказ
        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.NEW);

        // Добавляем товары
        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.getProductId()));

            order.addItem(product, itemReq.getQuantity());
        }

        return orderRepository.save(order);
    }

    // Получение всех заказов покупателя
    public List<OrderDto> getCustomerOrders(UUID customerId) {
        var orders = orderRepository.findByCustomerId(customerId);
        List<OrderDto> ordersDto = new ArrayList<>();
        orders.forEach(order -> ordersDto.add(orderMapper.toDto(order)));
        return ordersDto;
    }

    // Получение заказа с деталями
    public Order getOrderWithDetails(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Инициализируем ленивые коллекции
        Hibernate.initialize(order.getItems());

        return order;
    }

    // Обновление статуса заказа
    public Order updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    // Отмена заказа
    public void cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.SHIPPED ||
                order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot cancel shipped or delivered order");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
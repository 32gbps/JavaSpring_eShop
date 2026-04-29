package project.Services;

import project.Models.Product.Order.Order;
import project.Models.Product.Order.OrderMapper;
import project.Models.Product.Product;
import project.Models.User.Person.Customer;
import project.Models.Product.Order.*;
import project.Models.User.User;
import project.Repositories.OrderRepository;
import project.Repositories.CustomerRepository;
import project.Repositories.ProductRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.Repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderMapper orderMapper;

    // Создание заказа
    public Order createOrder(CreateOrderRequest request) {
        // Находим покупателя
        User user = userRepository.findById(request.getPersonId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Customer customer = customerRepository.findByUsername(user.getUsername())
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
    public List<OrderDto> getCustomerOrders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Customer customer = customerRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Person not found"));


        var orders = orderRepository.findByCustomerId(customer.getId());
        List<OrderDto> ordersDto = new ArrayList<>();
        orders.forEach(order -> ordersDto.add(orderMapper.toDto(order)));
        return ordersDto;
    }

    // Получение заказа с деталями
    public Order getOrderWithDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Инициализируем ленивые коллекции
        Hibernate.initialize(order.getItems());

        return order;
    }

    // Обновление статуса заказа
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    // Отмена заказа
    public void cancelOrder(Long orderId) {
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
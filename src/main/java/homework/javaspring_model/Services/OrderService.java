package homework.javaspring_model.Services;

import homework.javaspring_model.Models.Product.Order.CreateOrderRequest;
import homework.javaspring_model.Models.Product.Order.Order;
import homework.javaspring_model.Models.Product.Order.OrderItemRequest;
import homework.javaspring_model.Models.Product.Order.OrderStatus;
import homework.javaspring_model.Models.Product.Product;
import homework.javaspring_model.Models.User.Person.Person;
import homework.javaspring_model.Repositories.OrderRepository;
import homework.javaspring_model.Repositories.PersonRepository;
import homework.javaspring_model.Repositories.ProductRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ProductRepository productRepository;

    // Создание заказа
    public Order createOrder(CreateOrderRequest request) {
        // Находим покупателя
        Person person = personRepository.findById(request.getPersonId())
                .orElseThrow(() -> new RuntimeException("Person not found"));

        // Создаем заказ
        Order order = new Order();
        order.setPerson(person);
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
    public List<Order> getPersonOrders(Long personId) {
        return orderRepository.findByPersonId(personId);
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
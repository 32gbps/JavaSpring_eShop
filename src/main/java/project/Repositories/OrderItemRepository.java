package project.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.Models.Product.Order.OrderItem;
import project.Models.Product.Product;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    @Query("SELECT oi.product FROM OrderItem oi WHERE oi.order.id = :orderId")
    List<Product> findProductsByOrderId(@Param("orderId") Long orderId);
}
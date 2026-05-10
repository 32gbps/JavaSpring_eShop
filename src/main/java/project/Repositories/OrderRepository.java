package project.Repositories;

import project.Models.Product.Order.Order;
import project.Models.Product.Order.OrderStatus;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<@NonNull Order, @NonNull UUID> {

    @Query("SELECT o FROM Order o WHERE o.customer.customerId = :customerId")
    List<Order> findByCustomerId(@Param("customerId") UUID customerId);

    List<Order> findByStatus(OrderStatus status);

    Page<Order> findByCustomer_CustomerId(@Param("customerId") UUID customerId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findOrdersBetween(@Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate);
}

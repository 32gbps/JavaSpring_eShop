package homework.javaspring_model.Repositories;

import homework.javaspring_model.Models.Product.Order.Order;
import homework.javaspring_model.Models.Product.Order.OrderStatus;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<@NonNull Order, @NonNull Long> {

    @Query("SELECT o FROM Order o WHERE o.person.id = :personId")
    List<Order> findByPersonId(@Param("personId") Long personId);

    List<Order> findByStatus(OrderStatus status);

    Page<Order> findByPersonId(@Param("personId") Long personId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findOrdersBetween(@Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate);
}

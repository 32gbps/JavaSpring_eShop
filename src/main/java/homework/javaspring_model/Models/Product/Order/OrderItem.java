package homework.javaspring_model.Models.Product.Order;

import homework.javaspring_model.Models.Product.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price_at_order", nullable = false)
    private BigDecimal priceAtOrder;  // Цена на момент заказа (фиксируется)

    // Вычисляемое поле
    public BigDecimal getSubtotal() {
        return priceAtOrder.multiply(BigDecimal.valueOf(quantity));
    }
}
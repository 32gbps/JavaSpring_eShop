package project.Models.Product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reviewId;

    @Column(name = "customerId", nullable = false)
    private UUID customerId;
    @Column(name = "productId", nullable = false)
    private UUID productId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "positive")
    private String positive;
    @Column(name = "negative")
    private String negative;
    @Column(name = "description", nullable = false)
    @NotBlank(message = "Поле не может быть пустым")
    private String description;

    public ReviewDto ToDTO(){
        return new ReviewDto(this.getReviewId(), this.getCustomerId(),this.getProductId(), this.getCreatedAt(), this.getPositive(), this.getNegative(),this.getDescription());
    }

}

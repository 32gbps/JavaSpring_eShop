package homework.javaspring_model.Models.Product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "authorId", nullable = false)
    private Long userId;
    @Column(name = "productId", nullable = false)
    private Long productId;
    @Column(name = "positive")
    private String positive;
    @Column(name = "negative")
    private String negative;
    @Column(name = "description", nullable = false)
    @NotBlank(message = "Поле не может быть пустым")
    private String description;

    public static Review getEntityFromDto(ReviewDto dto){
        Review entity = new Review();
        entity.setId(dto.getId());
        entity.setUserId(dto.getUserId());
        entity.setProductId(dto.getProductId());
        entity.setPositive(dto.getPositive());
        entity.setNegative(dto.getNegative());
        entity.setDescription(dto.getDescription());
        return entity;
    }

}

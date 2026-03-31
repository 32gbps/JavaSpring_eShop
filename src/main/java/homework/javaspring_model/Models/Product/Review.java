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
    @Column(name = "positive")
    private String positive;
    @Column(name = "negative")
    private String negative;
    @Column(name = "description", nullable = false)
    @NotBlank(message = "Поле не может быть пустым")
    private String description;

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    private List<Comment> comments;
}

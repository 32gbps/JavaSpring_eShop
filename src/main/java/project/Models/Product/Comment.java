package project.Models.Product;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "reviewId", nullable = false)
    private Long reviewId;
    @Column(name = "userId", nullable = false)
    private Long userId;
    @Column(name = "text", nullable = false, length = 512)
    private String text;

    public static Comment getEntityFromDto(CommentDto dto){
        var entity = new Comment();
        entity.reviewId = dto.getReviewId();
        entity.userId = dto.getUserId();
        entity.text = dto.getText();
        return entity;
    }
}

package project.Models.Product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID commentId;
    @Column(name = "reviewId", nullable = false)
    private UUID reviewId;
    @Column(name = "userId", nullable = false)
    private UUID customerId;
    @Column(name = "text", nullable = false, length = 512)
    private String text;

    public CommentDto ToDTO(){
        return new CommentDto(this.getCommentId(),
                                this.getReviewId(),
                                this.getCommentId(),
                                this.getText());
    }
}

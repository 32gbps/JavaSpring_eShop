package project.Models.Product;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private Long reviewId;
    private Long userId;
    @NotBlank
    private String text;

    public static CommentDto getDtoFromEntity(Comment entity){
        var dto = new CommentDto();
        dto.reviewId = entity.getReviewId();
        dto.userId = entity.getUserId();
        dto.text = entity.getText();
        return dto;
    }
}

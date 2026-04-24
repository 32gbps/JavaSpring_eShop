package homework.javaspring_model.Models.Product;

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
        dto.setReviewId(entity.getReviewId());
        dto.setUserId(entity.getUserId());
        dto.setText(entity.getText());
        return dto;
    }
}

package project.Models.Product;
import lombok.Data;

@Data
public class ReviewDto {
    private Long id;
    private Long userId;
    private Long productId;
    private String positive;
    private String negative;
    private String description;
    private Long commentCount;

    public static ReviewDto getDtoFromEntity(Review entity){
        ReviewDto dto = new ReviewDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setProductId(entity.getProductId());
        dto.setPositive(entity.getPositive());
        dto.setNegative(entity.getNegative());
        dto.setDescription(entity.getDescription());
        return dto;
    }
}
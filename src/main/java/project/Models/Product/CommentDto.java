package project.Models.Product;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CommentDto(UUID commentId, UUID reviewId, UUID customerId, @NotBlank String text) {
}

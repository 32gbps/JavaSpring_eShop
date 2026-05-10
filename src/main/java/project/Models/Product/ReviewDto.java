package project.Models.Product;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewDto(UUID reviewId,
                        UUID customerId,
                        UUID productId,
                        LocalDateTime createdAt,
                        String positive,
                        String negative,
                        String description){}
package project.Repositories;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Models.Product.Review;
import project.Models.Product.ReviewDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<@NonNull Review, @NonNull UUID> {

    Optional<Review> findByCustomerId(UUID id);
    List<Review> findByProductId(UUID Id);

    Long countAllByProductId(UUID productId);

    ReviewDto findByCustomerIdAndProductId(UUID customerId, UUID productId);
}

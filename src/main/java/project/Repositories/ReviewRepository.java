package project.Repositories;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Models.Product.Review;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<@NonNull Review, @NonNull UUID> {

    Optional<Review> findByUserId(UUID id);
    List<Review> findByProductId(Long Id);

    Long countAllByProductId(Long productId);
}

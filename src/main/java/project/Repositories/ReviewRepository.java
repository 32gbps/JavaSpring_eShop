package project.Repositories;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Models.Product.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<@NonNull Review, @NonNull Long> {

    Optional<Review> findByUserId(Long id);
    List<Review> findByProductId(Long Id);

    Long countAllByProductId(Long productId);
}

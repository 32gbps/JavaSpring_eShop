package homework.javaspring_model.Repositories;

import homework.javaspring_model.Models.Product.Review;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<@NonNull Review, @NonNull Long> {

    Optional<Review> findByUserId(Long id);
    List<Review> findByProductId(Long Id);

    Long countAllByProductId(Long productId);
}

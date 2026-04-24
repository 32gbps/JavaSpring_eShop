package homework.javaspring_model.Repositories;

import homework.javaspring_model.Models.Product.Comment;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<@NonNull Comment, @NonNull Long> {
    Optional<Comment> findByUserId(Long id);
    List<Comment> findByReviewId(Long Id);
    Long countAllByReviewId(Long id);
}

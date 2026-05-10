package project.Repositories;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Models.Product.Comment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<@NonNull Comment, @NonNull UUID> {
    Optional<Comment> findByCustomerId(UUID id);
    List<Comment> findByReviewId(UUID Id);
    Long countAllByReviewId(UUID id);
}

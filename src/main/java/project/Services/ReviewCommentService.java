package project.Services;
import project.Models.Product.Comment;
import project.Models.Product.CommentDto;
import project.Models.Product.Review;
import project.Models.Product.ReviewDto;
import project.Repositories.CommentRepository;
import project.Repositories.ReviewRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class ReviewCommentService {
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;

    private static final Logger log = LoggerFactory.getLogger(ReviewCommentService.class);

    public Optional<Review> addReview(Review review){
        try{
            review.setCreatedAt(LocalDateTime.now());
            return Optional.of(reviewRepository.save(review));
        } catch (Exception e) {
            log.info(e.toString());
            return Optional.empty();
        }
    }
    public Optional<Review> addReview(ReviewDto dto){
        try{
            var r = new Review();
            r.setCustomerId(dto.customerId());
            r.setProductId(dto.productId());
            r.setPositive(dto.positive());
            r.setNegative(dto.negative());
            r.setDescription(dto.description());
            return addReview(r);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    public Optional<ReviewDto> findReviewsById(UUID id){
        try{
            return Optional.of(reviewRepository.findById(id).orElseThrow().ToDTO());
        }
        catch (Exception e){
            return Optional.empty();
        }
    }
    public Optional<ReviewDto> findReviewsByCustomerIdAndProductId(UUID customerId, UUID productId){
        try{
            return Optional.of(reviewRepository.findByCustomerIdAndProductId(customerId, productId));
        }
        catch (Exception e){
            return Optional.empty();
        }
    }
    public Optional<ReviewDto> findReviewsByCustomerId(UUID id){
        try{
            return Optional.of(reviewRepository.findByCustomerId(id).orElseThrow().ToDTO());
        }
        catch (Exception e){
            return Optional.empty();
        }
    }
    public List<ReviewDto> findAllReviewsByProductId(UUID id){
        try{
            var nList = new ArrayList<ReviewDto>();
            reviewRepository.findByProductId(id).forEach(r->{
                nList.add(r.ToDTO());
            });
            return nList;
        }
        catch (Exception e){
            return new ArrayList<>();
        }
    }
    public Long countAllReviewsByProductId(UUID id){
        return reviewRepository.countAllByProductId(id);
    }

    public Comment addComment(CommentDto commentDto){
        try{
            var comment = new Comment();
            comment.setReviewId(commentDto.reviewId());
            comment.setCustomerId(commentDto.customerId());
            comment.setText(comment.getText());
            return commentRepository.save(comment);
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }
    public Optional<CommentDto> findCommentsById(UUID id){
        try{
            return Optional.of(commentRepository.findById(id).orElseThrow().ToDTO());
        }
        catch (Exception e){
            return Optional.empty();
        }
    }
    public Optional<CommentDto> findCommentsByUserId(UUID id){
        try{
            return Optional.of(commentRepository.findByCustomerId(id).orElseThrow().ToDTO());
        }
        catch (Exception e){
            return Optional.empty();
        }
    }
    public List<CommentDto> findAllCommentsByProductId(UUID id){
        try{
            var nList = new ArrayList<CommentDto>();
            commentRepository.findByReviewId(id).forEach(c->nList.add(c.ToDTO()));
            return nList;
        }
        catch (Exception e){
            return new ArrayList<>();
        }
    }
    public Long countAllCommentsByProductId(UUID id){
        return commentRepository.countAllByReviewId(id);
    }
}

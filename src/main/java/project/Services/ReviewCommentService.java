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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ReviewCommentService {
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;

    private static final Logger log = LoggerFactory.getLogger(ReviewCommentService.class);

    public Review addReview(ReviewDto reviewDto){
        try{
            return reviewRepository.save(Review.getEntityFromDto(reviewDto));
        } catch (Exception e) {
            log.info("======================== EXCEPTION! ==============================");
            log.info(e.toString());
            log.info("======================== END! ==============================");
            return null;
        }
    };
    public Optional<ReviewDto> findReviewsById(Long id){
        try{
            var dto = ReviewDto.getDtoFromEntity(reviewRepository.findById(id).orElseThrow());
            dto.setCommentCount(countAllCommentsByProductId(dto.getProductId()));
            return Optional.of(dto);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }
    public Optional<ReviewDto> findReviewsByUserId(Long id){
        try{
            var dto = ReviewDto.getDtoFromEntity(reviewRepository.findByUserId(id).orElseThrow());
            dto.setCommentCount(countAllCommentsByProductId(dto.getProductId()));
            return Optional.of(dto);
        }
        catch (Exception e){
            return Optional.empty();
        }
    }
    public List<ReviewDto> findAllReviewsByProductId(Long id){
        try{
            var nList = new ArrayList<ReviewDto>();
            reviewRepository.findByProductId(id).forEach(r->{
                var dto = ReviewDto.getDtoFromEntity(r);
                dto.setCommentCount(countAllCommentsByProductId(dto.getProductId()));
                nList.add(dto);
            });
            return nList;
        }
        catch (Exception e){
            return new ArrayList<>();
        }
    }
    public Long countAllReviewsByProductId(Long id){
        return reviewRepository.countAllByProductId(id);
    }

    public Comment addComment(CommentDto commentDto){
        try{
            return commentRepository.save(Comment.getEntityFromDto(commentDto));
        } catch (Exception e) {
            log.info("======================== EXCEPTION! ==============================");
            log.info(e.toString());
            log.info("======================== END! ==============================");
            return null;
        }
    }
    public Optional<CommentDto> findCommentsById(Long id){
        try{
            return Optional.of(CommentDto.getDtoFromEntity(commentRepository.findById(id).orElseThrow()));
        }
        catch (Exception e){
            return Optional.empty();
        }
    }
    public Optional<CommentDto> findCommentsByUserId(Long id){
        try{
            return Optional.of(CommentDto.getDtoFromEntity(commentRepository.findByUserId(id).orElseThrow()));
        }
        catch (Exception e){
            return Optional.empty();
        }
    }
    public List<CommentDto> findAllCommentsByProductId(Long id){
        try{
            var nList = new ArrayList<CommentDto>();
            commentRepository.findByReviewId(id).forEach(c->nList.add(CommentDto.getDtoFromEntity(c)));
            return nList;
        }
        catch (Exception e){
            return new ArrayList<>();
        }
    }
    public Long countAllCommentsByProductId(Long id){
        return commentRepository.countAllByReviewId(id);
    }
}

package homework.javaspring_model.Controllers;

import homework.javaspring_model.Models.ApiResponse;
import homework.javaspring_model.Models.Product.CommentDto;
import homework.javaspring_model.Models.Product.ProductDto;
import homework.javaspring_model.Models.Product.ProductMapper;
import homework.javaspring_model.Models.Product.ReviewDto;
import homework.javaspring_model.Services.ProductService;
import homework.javaspring_model.Services.ReviewCommentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ApiController {
    private final ProductService Service;
    private final ReviewCommentService revComService;

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;

    @GetMapping(value = "/list", params = {"page", "size"})
    public ResponseEntity<?> getProducts(@RequestParam Integer page, @RequestParam Integer size) {
        try {
            int currentPage = (page != null) ? page : DEFAULT_PAGE;
            int pageSize = (size != null) ? size : DEFAULT_SIZE;

            var listOfProducts = Service.getFirstNElements(currentPage, pageSize);

            return ResponseEntity.ok(new ApiResponse("success", "", listOfProducts));
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Ошибка на сервере"));
        }
    }
    @GetMapping("/getProductById/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            String status = "success";
            ProductDto dto = Service.findById(id).orElseThrow();

            return ResponseEntity.ok(new ApiResponse(status, "",dto));
        }
        catch (NoSuchElementException e) {
        return ResponseEntity.ok(
                new ApiResponse("fail", "Товар с данным ID не найден!"));
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Ошибка на сервере"));
        }
    }
    @PostMapping("/deleteProductById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            Service.deleteProductById(id);

            String status = "success";
            String message = "Запрос получен";

            return ResponseEntity.ok(new ApiResponse(status, message));

        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse("error", "Ошибка при удалении: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {
        try {
            String status = "fail";
            String message = "";

            var res = Service.addProduct(ProductMapper.DtoToEntity(productDto));

            if(res.isPresent())
            {
                status = "success";
                message = "Предмет успешно добавлен!";
            }

            return ResponseEntity.ok(new ApiResponse(status, message));

        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @PostMapping("/addReview")
    public ResponseEntity<?> addReview(@RequestBody ReviewDto reviewDto) {
        try {
            var res = revComService.addReview(reviewDto);
            return ResponseEntity.ok(new ApiResponse("success", ""));

        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @PostMapping("/addReviewComment")
    public ResponseEntity<?> addReviewComment(@RequestBody CommentDto commentDto) {
        try {
            var res = revComService.addComment(commentDto);
            return ResponseEntity.ok(new ApiResponse("success", ""));

        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @GetMapping("/getProductReviews/{id}")
    public ResponseEntity<?> getProductReviews(@PathVariable Long id) {
        try {
            var result = revComService.findAllReviewsByProductId(id);
            return ResponseEntity.ok(new ApiResponse("success", "", result));

        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @GetMapping("/getReviewComments/{id}")
    public ResponseEntity<?> getReviewComments(@PathVariable Long id) {
        try {
            var result = revComService.findAllCommentsByProductId(id);
            return ResponseEntity.ok(new ApiResponse("success", "", result));
        }
        catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
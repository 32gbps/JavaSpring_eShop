package project.Controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import project.Models.ApiResponse;
import project.Models.Product.*;
import project.Services.CustomerService;
import project.Services.ProductService;
import project.Services.ReviewCommentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {
    private final CustomerService customerService;
    private final ProductService Service;
    private final ReviewCommentService revComService;

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;

    @GetMapping(value = "/product/list", params = {"page", "size"})
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
    @GetMapping("/product/getProductById/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
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
    @PostMapping("/product/deleteProductById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
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
    @PostMapping("/product/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto dto) {
        try {

            Service.addProduct(ProductMapper.DtoToEntity(dto));
            String status = "success";
            String message = "Предмет успешно добавлен!";

            return ResponseEntity.ok(new ApiResponse(status, message));

        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @PostMapping("/product/addReview")
    public ResponseEntity<?> addReview(@RequestBody ReviewDto reviewDto) {
        try {
            revComService.addReview(reviewDto);
            return ResponseEntity.ok(new ApiResponse("success", ""));

        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @PostMapping("/product/addReviewComment")
    public ResponseEntity<?> addReviewComment(@RequestBody CommentDto commentDto) {
        try {
            revComService.addComment(commentDto);
            return ResponseEntity.ok(new ApiResponse("success", ""));

        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @GetMapping("/product/getProductReviews/{id}")
    public ResponseEntity<?> getProductReviews(@PathVariable UUID id) {
        try {
            var result = revComService.findAllReviewsByProductId(id);
            return ResponseEntity.ok(new ApiResponse("success", "", result));

        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @GetMapping("/product/getReviewComments/{id}")
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

    @GetMapping("customer-info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetails userDetails){
        try {
            if(userDetails == null)
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

            var customer = customerService.findByUsername(userDetails.getUsername()).orElseThrow();
            return ResponseEntity.ok(new ApiResponse("success", "", customer));
        }
        catch (Exception e) {
            log.error(e.getMessage());
            ApiResponse errorResponse = new ApiResponse("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
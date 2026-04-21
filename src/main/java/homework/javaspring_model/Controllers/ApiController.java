package homework.javaspring_model.Controllers;

import homework.javaspring_model.Config.DatabaseInitializer;
import homework.javaspring_model.Models.ApiResponse;
import homework.javaspring_model.Models.Product.Product;
import homework.javaspring_model.Models.Product.ProductDto;
import homework.javaspring_model.Models.Product.ProductMapper;
import homework.javaspring_model.Services.CompanyService;
import homework.javaspring_model.Services.ProductService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ApiController {
    private final ProductService Service;
    private final CompanyService Companies;
    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static final int DEFAULT_PAGE = 0;  // Страницы с 0
    private static final int DEFAULT_SIZE = 10; // 10 элементов на странице

    @GetMapping(value = "/list", params = {"page", "size"})
    public ResponseEntity<?> getProducts(@RequestParam Integer page,
                              @RequestParam Integer size) {
        try {
            int currentPage = (page != null) ? page : DEFAULT_PAGE;
            int pageSize = (size != null) ? size : DEFAULT_SIZE;

            var pArr = Service.getFirstNElements(currentPage, pageSize)
                        .toArray(new ProductDto[0]);

            var response = new ApiResponse("success", null, pArr);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse("error", "Ошибка: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/getProductById/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            AtomicReference<String> status = new AtomicReference<>("");
            AtomicReference<String> message = new AtomicReference<>("");
            AtomicReference<ProductDto> dto = new AtomicReference<>();
            ApiResponse response;

            Service.findById(id).ifPresentOrElse(p->{
                        dto.set(p);
                        status.set("success");
                    },
                    () -> {
                        status.set("fail");
                        message.set("Товар с данным ID не найден!");
                    });
            // Успешный ответ с сообщением
            response = new ApiResponse(status.get(), message.get(),dto.get());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Ответ с ошибкой
            ApiResponse errorResponse = new ApiResponse("error", "Ошибка: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
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
            // Ответ с ошибкой
            ApiResponse errorResponse = new ApiResponse("error", "Ошибка при удалении: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {
        try {
            AtomicReference<String> status = new AtomicReference<>("");
            AtomicReference<String> message = new AtomicReference<>("");
            ApiResponse response;

            Service.addProduct(ProductMapper.DtoToEntity(productDto)).ifPresentOrElse(p->{
                        status.set("success");
                        message.set("Предмет успешно добавлен!");
                    },
                    () -> {
                        status.set("fail");
                    });
            ;
            // Успешный ответ с сообщением
            response = new ApiResponse(status.get(), message.get());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

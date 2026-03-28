package homework.javaspring_model.Controllers;

import homework.javaspring_model.Models.ApiResponse;
import homework.javaspring_model.Models.Product.Product;
import homework.javaspring_model.Models.Product.ProductDto;
import homework.javaspring_model.Services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ApiController {
    private final ProductService Service;

    @GetMapping("/getProductById/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            AtomicReference<String> status = new AtomicReference<>("");
            AtomicReference<String> message = new AtomicReference<>("");
            AtomicReference<ProductDto> dto = new AtomicReference<>();
            ApiResponse response;

            Service.findById(id).ifPresentOrElse(p->{
                        dto.set(new ProductDto(p));
                        status.set("success");
                        message.set("Предмет успешно удалён!");
                    },
                    () -> {
                        status.set("fail");
                        message.set("Товар с данным ID не найден!");
                    });
            // Успешный ответ с сообщением
            response = new ApiResponse(status.get(), message.get());
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
            AtomicReference<String> status = new AtomicReference<>("");
            AtomicReference<String> message = new AtomicReference<>("");
            ApiResponse response;

            Service.findById(id).ifPresentOrElse(p->{
                Service.deleteProduct(p);
                        status.set("success");
                        message.set("Предмет успешно удалён!");
                },
                    () -> {
                        status.set("fail");
                        message.set("Товар с данным ID не найден!");
                    });
            // Успешный ответ с сообщением
            response = new ApiResponse(status.get(), message.get());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Ответ с ошибкой
            ApiResponse errorResponse = new ApiResponse("error", "Ошибка при удалении: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    @PostMapping(value = "/addProduct")
    public ResponseEntity<?> addClothes(@RequestBody ProductDto dto) {
        try {
            AtomicReference<String> status = new AtomicReference<>("");
            AtomicReference<String> message = new AtomicReference<>("");
            ApiResponse response;

            Service.findByName(dto.getName()).ifPresentOrElse(p->{
                        status.set("fail");
                        message.set("Товар с таким названием уже существует!");
                    },
                    () -> {
                        Service.addProduct(new Product(dto)).orElseThrow();
                        status.set("success");
                        message.set("Предмет успешно добавлен!");
                    });
            // Успешный ответ с сообщением
            response = new ApiResponse(status.get(), message.get());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ApiResponse errorResponse = new ApiResponse("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

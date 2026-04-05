package homework.javaspring_model.Controllers;

import homework.javaspring_model.Models.ApiResponse;
import homework.javaspring_model.Models.Product.Product;
import homework.javaspring_model.Models.Product.ProductDto;
import homework.javaspring_model.Services.CompanyService;
import homework.javaspring_model.Services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ApiController {
    private final ProductService Service;
    private final CompanyService Companies;

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
    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {
        try {
            AtomicReference<String> status = new AtomicReference<>("");
            AtomicReference<String> message = new AtomicReference<>("");
            ApiResponse response;
            //TODO: Реализовать бэк для добавления товара с фронта: 1. Связывание товара с компанией, которая прислала товар.
            Service.findByName(productDto.getName()).ifPresentOrElse(p->{
                        status.set("fail");
                        message.set("Товар с таким названием уже существует!");
                    },
                    () -> {
                        var username = "company";
                        var company = Companies.findByUsername(username);
                        var product = new Product(productDto);
                        product.setCompany(company.orElseThrow());
                        Service.addProduct(product).orElseThrow();
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

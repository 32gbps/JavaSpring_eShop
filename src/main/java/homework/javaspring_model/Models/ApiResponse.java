package homework.javaspring_model.Models;

import lombok.Getter;
import lombok.Setter;

// Общий класс для ответов API
@Setter
@Getter
public class ApiResponse {
    // Геттеры и сеттеры
    private String status;
    private String message;
    private Object data;

    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiResponse(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}

package project.Models.Product.Order;

import lombok.Getter;

@Getter
public enum OrderStatus {
    NEW("Новый"),
    CONFIRMED("Подтверждён"),
    PROCESSING("В обработке"),
    SHIPPED("Отправлен"),
    DELIVERED("Доставлен"),
    CANCELLED("Отменён");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
package homework.javaspring_model.Models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DTOClothes {
    private Long id;        // Только если нужно передавать ID
    private String name;
    private String type;
    private String size;
    private String color;
    private String brand;
    private Double price;

    // Конструктор из Entity
    public DTOClothes(Clothes clothes) {
        this.id = clothes.getId();
        this.name = clothes.getName();
        this.type = clothes.getType();
        this.size = clothes.getSize();
        this.color = clothes.getColor();
        this.brand = clothes.getBrand();
        this.price = clothes.getPrice();
    }

    // Метод для преобразования в Entity
    public Clothes toEntity() {
        Clothes clothes = new Clothes();
        clothes.setName(this.name);
        clothes.setType(this.type);
        clothes.setSize(this.size);
        clothes.setColor(this.color);
        clothes.setBrand(this.brand);
        clothes.setPrice(this.price);
        return clothes;
    }

    // Для обновления существующей сущности
    public void updateEntity(Clothes clothes) {
        clothes.setName(this.name);
        clothes.setType(this.type);
        clothes.setSize(this.size);
        clothes.setColor(this.color);
        clothes.setBrand(this.brand);
        clothes.setPrice(this.price);
    }
}

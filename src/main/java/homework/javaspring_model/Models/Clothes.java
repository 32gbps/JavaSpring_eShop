package homework.javaspring_model.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "clothes")
public class Clothes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Поле не может быть пустым")
    private String name;

    @Column(name = "type", nullable = false)
    @NotBlank(message = "Поле не может быть пустым")
    private String type;

    @Column(name = "size", nullable = false)
    @NotBlank(message = "Поле не может быть пустым")
    private String size;

    @Column(name = "color", nullable = false)
    @NotBlank(message = "Поле не может быть пустым")
    private String color;

    @Column(name = "brand", nullable = false)
    @NotBlank(message = "Поле не может быть пустым")
    private String brand;

    @Column(name = "price", nullable = false)
    @Min(value = 0, message = "Цена не может быть отрицательной")
    private Double price;

    //Копирующий конструктор
    public Clothes(Clothes ref) {
        this.id = null;
        this.name = ref.name;
        this.type = ref.type;
        this.size = ref.size;
        this.color = ref.color;
        this.brand = ref.brand;
        this.price = ref.price;
    }

    public Clothes(String name, String type, String size,
                   String color, String brand, Double price) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.color = color;
        this.brand = brand;
        this.price = price;
    }

    public boolean equals(Clothes obj) {
        if (obj == null)
            return false;
        return this.name.equals(obj.name) &&
                this.type.equals(obj.type) &&
                this.size.equals(obj.size) &&
                this.color.equals(obj.color) &&
                this.brand.equals(obj.brand);
    }
}
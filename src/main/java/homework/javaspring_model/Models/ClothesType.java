package homework.javaspring_model.Models;

public enum ClothesType {
    PANTS("Штаны"),
    SNEAKERS("Кеды"),
    SOCKS("Носки"),
    CAP("Кепка"),
    T_SHIRT("Футболка"),
    SHIRT("Рубашка"),
    JACKET("Куртка"),
    DRESS("Платье"),
    SKIRT("Юбка"),
    SWEATER("Свитер"),
    HOODIE("Толстовка"),
    SHORTS("Шорты"),
    COAT("Пальто");

    public final String displayName;

    ClothesType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
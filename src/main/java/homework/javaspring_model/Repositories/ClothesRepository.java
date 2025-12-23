package homework.javaspring_model.Repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import homework.javaspring_model.Models.Clothes;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClothesRepository extends JpaRepository<Clothes, Long>{
    // Long - тип ID

    // Автоматически сгенерированные запросы по имени метода

    // SELECT * FROM clothesTable WHERE id = ?
    Optional<Clothes> findById(Long id);

    // SELECT * FROM clothesTable WHERE name LIKE %?%
    List<Clothes> findByName(String email);

    // SELECT * FROM clothesTable WHERE type = ?
    List<Clothes> findByType(String type);

    // SELECT * FROM clothesTable WHERE size = ?
    List<Clothes> findBySize(String size);

    // SELECT * FROM clothesTable WHERE color = ?
    Optional<Clothes> findByColor(String color);

    // SELECT * FROM clothesTable WHERE size = ?
    List<Clothes> findByBrand(String brand);

    // SELECT * FROM clothesTable WHERE price >= min AND price <= max
    List<Clothes> findByPriceBetween(Double min, Double max);

    // SELECT * FROM clothesTable WHERE name LIKE %?%
    List<Clothes> getDistinctTopByName(String email);
//    // Кастомный запрос с JPQL
//    @Query("SELECT u FROM ClothesTable u WHERE u.email LIKE %:domain")
//    List<User> findByEmailDomain(@Param("domain") String domain);
//
//    // Нативный SQL запрос
//    @Query(value = "SELECT * FROM users WHERE created_at > :date", nativeQuery = true)
//    List<User> findUsersAfterDate(@Param("date") LocalDateTime date);
    // Нативный SQL запрос
    @Modifying //@Modifying - указывает, что запрос изменяет данные (INSERT, UPDATE, DELETE)
    @Transactional //@Transactional - обеспечивает выполнение в транзакции

    @Query(value = "INSERT INTO clothes (name, type, size, color, brand, price) " +
            "VALUES (:name, :type, :size, :color, :brand, :price)",
            nativeQuery = true)
    int insertClothes(@Param("name") String name,
                       @Param("type") String type,
                       @Param("size") String size,
                       @Param("color") String color,
                       @Param("brand") String brand,
                       @Param("price") Double price);
//    @Modifying
//    @Transactional //@Transactional - обеспечивает выполнение в транзакции
//    @Query(value = "SELECT TOP(:n) FROM clothesTable", nativeQuery = true)
//    List<Clothes> getTopByCount(@Param("n") long n);

    // Проверка существования
    boolean existsById(Long id);
    //@Modifying
    @Query("SELECT c FROM Clothes c WHERE " +
            "LOWER(c.name) = LOWER(:name) AND " +
            "LOWER(c.type) = LOWER(:type) AND " +
            "LOWER(c.size) = LOWER(:size) AND " +
            "LOWER(c.color) = LOWER(:color) AND " +
            "LOWER(c.brand) = LOWER(:brand)")
    Optional<Clothes> findByParametersIgnoreCase(
            @Param("name") String name,
            @Param("type") String type,
            @Param("size") String size,
            @Param("color") String color,
            @Param("brand") String brand);

    @Query("SELECT c FROM Clothes c ORDER BY c.name")
    List<Clothes> findTopN(Pageable pageable);

    @Query("SELECT c FROM Clothes c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY c.name")
    List<Clothes> findByNameContainingIgnoreCaseOrderByName(@Param("name") String name, Pageable pageable);
}
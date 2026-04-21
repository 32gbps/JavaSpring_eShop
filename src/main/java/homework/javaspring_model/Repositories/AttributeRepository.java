package homework.javaspring_model.Repositories;

import homework.javaspring_model.Models.Product.Attribute;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttributeRepository extends JpaRepository<@NonNull Attribute, @NonNull Long> {
    Optional<Attribute> findByName(String name);
}
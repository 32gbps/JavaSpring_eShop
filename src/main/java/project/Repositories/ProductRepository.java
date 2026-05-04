package project.Repositories;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Models.Product.Product;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository  extends JpaRepository<@NonNull Product, @NonNull UUID> {
    Optional<Product> findByName(String name);
}

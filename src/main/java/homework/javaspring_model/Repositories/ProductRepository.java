package homework.javaspring_model.Repositories;

import homework.javaspring_model.Models.Product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository  extends JpaRepository<ProductEntity, Long> {
    @Override
    boolean existsById(Long aLong);
}

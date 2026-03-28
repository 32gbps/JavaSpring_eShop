package homework.javaspring_model.Repositories;

import homework.javaspring_model.Models.User.Company.Company;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<@NonNull Company, @NonNull Long> {
    Optional<Company> findByName(String name);
}

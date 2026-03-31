package homework.javaspring_model.Repositories;

import homework.javaspring_model.Models.User.Company.Company;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<@NonNull Company, @NonNull Long> {
    Optional<Company> findByName(String name);

    @Query("SELECT C FROM Company AS C WHERE C.user.username = :name")
    Optional<Company> findByUsername(@Param("name") String name);
}

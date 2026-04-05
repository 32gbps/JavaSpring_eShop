package homework.javaspring_model.Repositories;

import homework.javaspring_model.Models.User.Company.Company;
import homework.javaspring_model.Models.User.Person.Person;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository  extends JpaRepository<@NonNull Person, @NonNull Long> {
    Optional<Person> findByName(String name);

    @Query("SELECT P FROM Person AS P WHERE P.user.username = :username")
    Optional<Person> findByUsername(@Param("username") String username);
}

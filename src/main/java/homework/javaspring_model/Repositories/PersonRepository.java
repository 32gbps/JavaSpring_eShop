package homework.javaspring_model.Repositories;

import homework.javaspring_model.Models.User.Person.Person;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository  extends JpaRepository<@NonNull Person, @NonNull Long> {
    Optional<Person> findByName(String name);
}

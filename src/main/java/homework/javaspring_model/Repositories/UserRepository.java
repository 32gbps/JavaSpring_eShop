package homework.javaspring_model.Repositories;

import homework.javaspring_model.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    List<User> getUsersById(Long id);

    List<User> getUsersByIdGreaterThan(Long idIsGreaterThan);
}

package project.Repositories;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Models.User.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<@NonNull Role, @NonNull Long> {
    Optional<Role> findByName(String name);
}

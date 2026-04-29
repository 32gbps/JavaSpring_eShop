package project.Repositories;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.Models.User.Person.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<@NonNull Customer, @NonNull Long> {
    Optional<Customer> findByName(String name);

    @Query("SELECT P FROM Customer AS P WHERE P.user.username = :username")
    Optional<Customer> findByUsername(@Param("username") String username);
}

package project.Repositories;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.Models.User.Vendor.Vendor;
import java.util.Optional;
import java.util.UUID;

public interface VendorRepository extends JpaRepository<@NonNull Vendor, @NonNull UUID> {
    Optional<Vendor> findByVendorName(String vendorName);

    @Query("SELECT C FROM Vendor AS C WHERE C.user.username = :name")
    Optional<Vendor> findByUsername(@Param("name") String name);
}

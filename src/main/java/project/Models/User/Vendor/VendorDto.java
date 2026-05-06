package project.Models.User.Vendor;

import java.util.UUID;

/**
 * @param identifier "xxxx-xxxx-xxxx-xxxx"
 */
public record VendorDto(UUID id, String vendorName, String identifier, String username, String email, String password) {
    @Override
    public String toString() {
        return String.format("""
                id: %s
                vendorName: %s
                identifier: %s
                username: %s
                email: %s
                """, this.id, this.vendorName, this.identifier, this.username, this.email);
    }
}

package project.Models.User.Vendor;

import lombok.Getter;
import lombok.Setter;
import project.Models.User.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "vendors")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", unique = true)
    private String vendorName;

    @Column(name = "identifier")
    private String identifier;  //"xxxx-xxxx-xxxx-xxxx"

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    public VendorDto ToDTO(){
        return new VendorDto(id, vendorName, identifier, user.getUsername(), user.getEmail(), user.getPassword());
    }
}

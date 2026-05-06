package project.Models.User.Customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Value
@AllArgsConstructor
public class CustomerDto {

    UUID id;
    String name;
    String surname;
    @JsonFormat(pattern = "dd.MM.yyyy")
    LocalDate birthDate;
    String username;
    String email;
    String password;
}

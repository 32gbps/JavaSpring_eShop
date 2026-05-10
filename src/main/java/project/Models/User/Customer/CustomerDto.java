package project.Models.User.Customer;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.UUID;

public record CustomerDto(UUID id,
                          String name,
                          String surname,
                          @JsonFormat(pattern = "dd.MM.yyyy") LocalDate birthDate,
                          String username,
                          String email,
                          String password) {

}

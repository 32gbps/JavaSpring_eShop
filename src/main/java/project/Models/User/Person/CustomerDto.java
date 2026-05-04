package project.Models.User.Person;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;
import project.Models.Product.ProductDto;
import project.Models.User.UserDto;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Value
@AllArgsConstructor
public class CustomerDto {

    Long Id;
    UserDto User;
    String Name;
    String Surname;
    @JsonFormat(pattern = "dd.MM.yyyy")
    LocalDate birthDate;
    List<ProductDto> products = new ArrayList<>();
}

package project.Models.User.Person;

import com.fasterxml.jackson.annotation.JsonFormat;
import project.Models.Product.ProductDto;
import project.Models.User.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class CustomerDto {

    Long Id;
    UserDto User;
    String Name;
    String Surname;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;
    private List<ProductDto> products = new ArrayList<>();
}

package homework.javaspring_model.Models.User.Person;

import com.fasterxml.jackson.annotation.JsonFormat;
import homework.javaspring_model.Models.User.UserDto;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
public class PersonDto{
    Long Id;
    UserDto User;
    String Name;
    String Surname;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;

}

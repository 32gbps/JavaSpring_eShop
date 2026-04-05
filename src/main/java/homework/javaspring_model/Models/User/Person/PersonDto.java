package homework.javaspring_model.Models.User.Person;

import com.fasterxml.jackson.annotation.JsonFormat;
import homework.javaspring_model.Models.Product.Product;
import homework.javaspring_model.Models.User.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class PersonDto{
    public PersonDto(Person person){
        this.Id = person.getId();
        this.User = new UserDto(person.getUser());
        this.Name = person.getName();
        this.Surname = person.getSurname();
        this.birthDate = person.getBirthDate();
        this.products = person.getProducts();
    }
    Long Id;
    UserDto User;
    String Name;
    String Surname;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;
    private List<Product> products = new ArrayList<>();
}

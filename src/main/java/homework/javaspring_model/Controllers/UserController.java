package homework.javaspring_model.Controllers;

import homework.javaspring_model.Models.UserDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public UserDto addUser(@RequestBody @Validated UserDto userDto) {
        return userDto;
    }
}
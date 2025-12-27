package homework.javaspring_model.Controllers;

import homework.javaspring_model.Models.Clothes;
import homework.javaspring_model.Models.ClothesType;
import homework.javaspring_model.Models.User;
import homework.javaspring_model.Services.ClothesService;
import net.datafaker.Faker;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final ClothesService clothesService;
    private final Faker faker;

    public ProfileController(ClothesService clothesService) {
        faker = new Faker();
        this.clothesService = clothesService;
    }

    @GetMapping()
    public String getProfile(Model model) {
        model.addAttribute("title", "Личный кабинет");
        var user = new User();
        user.setId(42L);
        user.setName(faker.name().firstName());
        user.setSurname(faker.name().lastName());
        user.setEmail(faker.internet().emailAddress());
        model.addAttribute("user", user);
        return "profile";
    }
    @GetMapping("/adminPanel")
    public String getAdminPanel(Model model) {

        model.addAttribute("title", "Панель администратора");
        var c = new Clothes();
        c.setId(-1L);
        model.addAttribute("clothes", c);
        model.addAttribute("clothesList", clothesService.getFirstNElements(0, 10));
        return "adminPanel";
    }
    @GetMapping("/fillBaseRandObjects{count}")
    public String fillBase(int count) {
        Random r = new Random();
        String[] size = new String[]{"XS", "S", "M", "L", "XL", "XXL"};
        int i = 0;
        int added = 0;
        while(i < count) {
            var c = new Clothes(
                    faker.commerce().productName(),
                    ClothesType.values()[r.nextInt(ClothesType.values().length)].getDisplayName(),
                    size[r.nextInt(size.length)],
                    faker.color().name(),
                    faker.commerce().brand(),
                    Double.parseDouble(faker.commerce().price(10.0, 3000.0).replace(',','.')));

            if(!clothesService.isExistByName(c.getName()))
            {
                clothesService.addClothes(c);
                ++added;
            }

            ++i;
        }
        IO.println("Added objects into db: " + added);
        return "redirect:/profile/adminPanel";
    }
}

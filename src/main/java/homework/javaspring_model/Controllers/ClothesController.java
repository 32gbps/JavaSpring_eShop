package homework.javaspring_model.Controllers;

import homework.javaspring_model.Models.Clothes;

import homework.javaspring_model.Services.ClothesService;
import net.datafaker.Faker;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clothes")
public class ClothesController {

    private final  ClothesService clothesService;
    public ClothesController(ClothesService clothesService) {this.clothesService = clothesService;}

    @GetMapping({"/list", "/list/{page}", "/list/{page}/{size}"})
    public String showClothes(@PathVariable(required = false) Integer page, @PathVariable(required = false) Integer size, Model model) {
        int currentPage = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 10;

        model.addAttribute("title", "Список одежды");
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("clothesList", clothesService.getFirstNElements(currentPage, pageSize));

        IO.println("page: " + currentPage);
        IO.println("size: " + pageSize);

        return "clothes";
    }

    @PostMapping("/add")
    public String addClothes(@ModelAttribute Clothes clothes) {
        IO.println(String.format("Controller /add; form Clothes{id: %d}", clothes.getId()));

        clothesService.addClothes(clothes);
        return "redirect:/clothes/list";
    }
    @GetMapping("/edit/{id}")
    public String getEditClothesForm(@PathVariable int id, Model model) {
        var finded = clothesService.getClothesById(id);
        if(finded != null)
            model.addAttribute("clothes", finded);

        model.addAttribute("clothesList", clothesService.getFirstNElements(0, 25));
        return "clothes";
    }
    @PostMapping("/edit")
    public String editClothes(@ModelAttribute Clothes clothes) {
        clothesService.updateClothes(clothes.getId(), clothes);
        return "redirect:/clothes/list";
    }
    @GetMapping("/delete/{id}")
    public String deleteClothes(@PathVariable Long id) {
        clothesService.deleteClothes(id);

        return "redirect:/clothes/list";
    }
    @GetMapping("/filter")
    public String getFilteredList(String name, String color, String size, String type, String brand, Double price, Model model) {
        model.addAttribute("clothesList", clothesService.getFilteredList(name, color, size,type ,brand, price));
        return "clothes";
    }
}
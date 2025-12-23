package homework.javaspring_model.Controllers;

import homework.javaspring_model.Models.Clothes;
import homework.javaspring_model.Services.ClothesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clothes")
public class ClothesController {

    private final  ClothesService clothesService;

    public ClothesController(ClothesService clothesService) {
        this.clothesService = clothesService;
    }

    @GetMapping("/list")
    public String showClothes(Model model) {
        var c = new Clothes();
        c.setId(-1L);
        model.addAttribute("clothes", c);
        model.addAttribute("clothesList", clothesService.getFirstNElements(10));
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

        model.addAttribute("clothesList", clothesService.getFirstNElements(25));
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
}
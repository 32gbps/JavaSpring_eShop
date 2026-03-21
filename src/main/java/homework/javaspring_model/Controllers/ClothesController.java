package homework.javaspring_model.Controllers;

import homework.javaspring_model.Models.Clothes;

import homework.javaspring_model.Models.DTOClothes;
import homework.javaspring_model.Services.ClothesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clothes")
public class ClothesController {

    private final  ClothesService clothesService;
    public ClothesController(ClothesService clothesService) {this.clothesService = clothesService;}
    private static final int DEFAULT_PAGE = 0;  // Страницы с 0
    private static final int DEFAULT_SIZE = 10; // 10 элементов на странице

    @GetMapping(value = "/list", params = {"page", "size"})
    public String getClothesList(@RequestParam Integer page,
                             @RequestParam Integer size,
                             Model model) {
        //Пагинация, значения по-умолчанию
        int currentPage = (page != null) ? page : DEFAULT_PAGE;
        int pageSize = (size != null) ? size : DEFAULT_SIZE;

        //Данные отправляемые на страницу
        //Заголовок
        model.addAttribute("title", "Список одежды");
        //Пагинация. Текущая страница
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        //Список запрошенных объектов
        model.addAttribute("clothesList", clothesService.getFirstNElements(currentPage, pageSize));
        //Модель для фильтра
        model.addAttribute("filter", new Clothes());

        //для дебага
        IO.println("page: " + currentPage);
        IO.println("size: " + pageSize);

        //Отображаемый шаблон (clothes.html)
        return "clothes";
    }
    @GetMapping({"", "/", "/list"})
    public String GetRedirect() {
        return "redirect:/clothes/list?page=" + DEFAULT_PAGE + "&size=" + DEFAULT_SIZE;
    }
    @GetMapping("/product/{id}")
    public String getProductInfo(@PathVariable Long id, Model model) {
        if(!clothesService.isExistById(id))
            return "redirect:/clothes";

        var product = clothesService.getClothesById(id);
        model.addAttribute("productData", new DTOClothes(product));
        return "productInfoView";
    }
//Теперь через API
//    @PostMapping("/add")
//    public String addClothes(@ModelAttribute Clothes clothes) {
//        IO.println(String.format("Controller /add; form Clothes{id: %d}", clothes.getId()));
//
//        clothesService.addClothes(clothes);
//        return "redirect:/clothes/list";
//    }
//    @GetMapping(value = "/getClothes", params = {"id"})
//    public String getClotheById(@RequestParam Integer id, Model model) {
//        var finded = clothesService.getClothesById(id);
//        if(finded != null)
//            model.addAttribute("editable", finded);
//
//        return "adminPanel";
//    }
//    @PostMapping(value = "/edit", params = {"editable"})
//    public String editClothes(@RequestParam Clothes editable) {
//        clothesService.updateClothes(editable.getId(), editable);
//        return "adminPanel";
//    }
//    @GetMapping("/delete/{id}")
//    public String deleteClothes(@PathVariable Long id) {
//        clothesService.deleteClothes(id);
//
//        return "redirect:/clothes/list";
//    }


    @GetMapping("/filter")
    public String getFilteredList(@ModelAttribute Clothes filter, Model model) {
        var list = clothesService.getFilteredList(filter.name, filter.color, filter.size,filter.type ,filter.brand, filter.price);
        IO.println("list size: " + list.size());
        model.addAttribute("title", "Список одежды");
        //Список запрошенных объектов
        model.addAttribute("clothesList", list);
        //Модель для фильтра
        model.addAttribute("filter", new Clothes());

        return "clothes";
    }
}
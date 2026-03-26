package homework.javaspring_model.Controllers.View;

import homework.javaspring_model.Services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private static final int DEFAULT_PAGE = 0;  // Страницы с 0
    private static final int DEFAULT_SIZE = 10; // 10 элементов на странице


    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping(value = "/list", params = {"page", "size"})
    public String getProducts(@RequestParam Integer page,
                              @RequestParam Integer size,
                              Model model) {
        //Пагинация, значения по-умолчанию
        int currentPage = (page != null) ? page : DEFAULT_PAGE;
        int pageSize = (size != null) ? size : DEFAULT_SIZE;

        //Данные отправляемые на страницу

        //Пагинация. Текущая страница
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        try {
            //Список запрошенных объектов
            model.addAttribute("productsList", productService.getFirstNElements(currentPage, pageSize));
            return "catalog";
        } catch (Exception e) {
            model.addAttribute("message", "Непредвиденная ошибка; не удалось получить запрашиваемые данные.");
            //И в логи ещё записать. Но потом...
            return "redirect:/";
        }
    }
    @GetMapping({"", "/"})
    public String GetRedirect() {
        return "redirect:/product/list?page=" + DEFAULT_PAGE + "&size=" + DEFAULT_SIZE;
    }
    @GetMapping(value = "/", params = {"id"})
    public String getProductInfo(@RequestParam Long id, Model model) {
        try {
            if(!productService.isExistById(id)){
                model.addAttribute("message", "Товар не найден!");
                return "redirect:/";
            }

            var product = productService.getProductById(id);

            model.addAttribute("productData", product);
            return "productInfoView";
        } catch (Exception e) {
            model.addAttribute("message", "Внутренняя ошибка при запросе товара");
            return "redirect:/";
        }
    }
}

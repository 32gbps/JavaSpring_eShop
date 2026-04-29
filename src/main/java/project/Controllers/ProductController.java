package project.Controllers;

import project.Services.ProductService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @Autowired
    private MessageSource messageSource;
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private static final int DEFAULT_PAGE = 0;  // Страницы с 0
    private static final int DEFAULT_SIZE = 10; // 10 элементов на странице

    @GetMapping({"", "/"})
    public String GetRedirect() {
        return "redirect:/";
    }
    @GetMapping("/{id}")
    public String getProductInfo(@PathVariable Long id, Model model) {
        try {
            if(!productService.isExistById(id)){
                model.addAttribute("message", "Товар не найден!");
                return "redirect:/";
            }

            var product = productService.findById(id).orElseThrow();

            model.addAttribute("productData", product);

            return "productDetail";
        } catch (Exception e) {
            model.addAttribute("message", "Внутренняя ошибка при запросе товара");
            return "redirect:/";
        }
    }
    @GetMapping("/cart")
    public String getShoppingCart(Model model, Locale locale) {
        try {
            model.addAttribute("pageTitle", messageSource.getMessage("page.title.productCart", null, locale));
            model.addAttribute("initMethod", 2);
            model.addAttribute("message", null);

            return "index";
        } catch (Exception e) {
            model.addAttribute("message", "Внутренняя ошибка при запросе товара");
            return "redirect:/";
        }
    }
}

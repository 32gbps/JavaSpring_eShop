package project.Controllers;

import project.Services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;
import java.util.UUID;

@Controller
@AllArgsConstructor
@RequestMapping({"/",""})
public class ProductController {
    private final ProductService productService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping()
    public String index(Model model, Locale locale) {
        model.addAttribute("pageTitle", messageSource.getMessage("page.title.index", null, locale));
        model.addAttribute("initMethod", 0);
        model.addAttribute("message", null);

        return "index";
    }
    @GetMapping("/product/{id}")
    public String getProductInfo(@PathVariable UUID id, Model model) {
        try {
            var product = productService.findById(id).orElseThrow();

            model.addAttribute("productData", product);

            return "productDetail";
        } catch (Exception e) {
            model.addAttribute("message", "Внутренняя ошибка при запросе товара");
            return "redirect:/";
        }
    }
    @GetMapping("/product/cart")
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

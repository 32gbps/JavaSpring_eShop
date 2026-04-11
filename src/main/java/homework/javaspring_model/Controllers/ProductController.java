package homework.javaspring_model.Controllers;

import homework.javaspring_model.Config.DatabaseInitializer;
import homework.javaspring_model.Models.Product.ProductDto;
import homework.javaspring_model.Services.ProductService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.CookieManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static final int DEFAULT_PAGE = 0;  // Страницы с 0
    private static final int DEFAULT_SIZE = 10; // 10 элементов на странице

    @GetMapping(value = "/list", params = {"page", "size"})
    public String getProducts(@RequestParam Integer page,
                              @RequestParam Integer size,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              Model model) {
        //Пагинация, значения по-умолчанию
        int currentPage = (page != null) ? page : DEFAULT_PAGE;
        int pageSize = (size != null) ? size : DEFAULT_SIZE;

        //Данные отправляемые на страницу

        //Пагинация. Текущая страница
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        try {
            Long[] ids = productService.getFirstNElements(currentPage, pageSize)
                    .stream()
                    .map(ProductDto::getId)
                    .toArray(Long[]::new);
            String idsString = Arrays.stream(ids)
                    .map(String::valueOf)
                    .collect(Collectors.joining("-"));
            // Создаем куку
            log.info("=================== INFO START ========================");
            log.info("Cookie info:");
            Arrays.stream(request.getCookies()).toList().forEach(c->{
                log.info("{} :", c.toString());
                c.getAttributes().forEach( (k, v)->{
                    log.info("{} : {}", k, v);
                });
            });
            log.info("idsString: {}", idsString);
            log.info("=================== INFO END ========================");
            Cookie cookie = new Cookie("wishlist", idsString);
            cookie.setPath("/");
            cookie.setMaxAge(3600); // 30 дней
            //cookie.setHttpOnly(true);  // Защита от XSS
            //cookie.setSecure(false);    // Для HTTPS установить true
            log.info("cookie: {}", cookie.getValue());
            log.info("=================== INFO END ========================");

            response.addCookie(cookie);
            //model.addAttribute("productsList", );
            return "catalog";
        } catch (Exception e) {
            model.addAttribute("message", "Непредвиденная ошибка; не удалось получить запрашиваемые данные.");
            log.info("===================EXCEPTION INFO START========================");
            log.info(e.getMessage());
            log.info("===================EXCEPTION INFO END========================");
            //И в логи ещё записать. Но потом...
            return "redirect:/";
        }
    }
    @GetMapping({"", "/"})
    public String GetRedirect() {
        return "redirect:/product/list?page=" + DEFAULT_PAGE + "&size=" + DEFAULT_SIZE;
    }
    @GetMapping("/{id}")
    public String getProductInfo(@PathVariable Long id, Model model) {
        try {
            if(!productService.isExistById(id)){
                model.addAttribute("message", "Товар не найден!");
                return "redirect:/";
            }

            var product = productService.findById(id).orElseThrow();

            model.addAttribute("productData", new ProductDto(product));

            return "productDetail";
        } catch (Exception e) {
            model.addAttribute("message", "Внутренняя ошибка при запросе товара");
            return "redirect:/";
        }
    }
    @GetMapping("/cart")
    public String getShoppingCart(Model model) {
        try {

            return "shoppingCart";
        } catch (Exception e) {
            model.addAttribute("message", "Внутренняя ошибка при запросе товара");
            return "redirect:/";
        }
    }
}

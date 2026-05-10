package project.Controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.Models.Product.*;
import project.Services.CustomerService;
import project.Services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import project.Services.ReviewCommentService;

import java.util.*;

@Controller
@AllArgsConstructor
@RequestMapping({"/",""})
public class ProductController {
    private final ProductService productService;
    private final CustomerService customerService;
    private final ReviewCommentService ComRevService;

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
            var list = ComRevService.findAllReviewsByProductId(id);
            //if(!list.isEmpty())
            model.addAttribute("productReviews", list);

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
    @GetMapping("/product/review/{productId}")
    public String getReviewForm(@PathVariable UUID productId, Model model){
        try {
            var product = productService.findById(productId).orElseThrow();
            model.addAttribute("product", product);
            model.addAttribute("insertFragment", "reviewForm");
            model.addAttribute("buttonSet", "CUSTOMER");
            return "profile";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "redirect:/";
        }
    }
    @PostMapping("/product/addReview")
    public String addReview(@ModelAttribute ReviewDto reviewDto, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        try {
            var customerId = customerService.findByUsername(userDetails.getUsername()).orElseThrow().id();
            if(ComRevService.findReviewsByCustomerIdAndProductId(customerId, reviewDto.productId()).isPresent()){
                model.addAttribute("message", "Отзыв на данный товар от данного пользователя уже существует");
                return "redirect:/";
            }

            Review rev = new Review();
            rev.setProductId(reviewDto.productId());
            rev.setCustomerId(customerId);
            rev.setPositive(reviewDto.positive());
            rev.setNegative(reviewDto.negative());
            rev.setDescription(reviewDto.description());

            ComRevService.addReview(rev);
            return "redirect:/product/" + reviewDto.productId();
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "redirect:/";
        }
    }
}

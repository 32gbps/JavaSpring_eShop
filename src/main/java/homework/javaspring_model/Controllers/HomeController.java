package homework.javaspring_model.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Controller
public class HomeController {
    @Autowired
    private MessageSource messageSource;
    @GetMapping("/")
    public String index(Model model, Locale locale) {
        model.addAttribute("pageTitle", messageSource.getMessage("page.title.index", null, locale));
        model.addAttribute("initMethod", 0);
        model.addAttribute("message", null);

        return "index";
    }

    //test
    @GetMapping("/home")
    public String home() {
        return "redirect:/profile";
    }
}
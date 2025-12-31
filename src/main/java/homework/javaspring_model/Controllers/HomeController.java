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
        String greeting = messageSource.getMessage(
                "user.greeting",
                new Object[]{"Anonimous"},
                locale
        );

        model.addAttribute("welcomeMsg",
                messageSource.getMessage("welcome.message", null, locale));
        model.addAttribute("greeting", greeting);
        model.addAttribute("title",
                messageSource.getMessage("page.title", null, locale));

        return "index";
    }
}
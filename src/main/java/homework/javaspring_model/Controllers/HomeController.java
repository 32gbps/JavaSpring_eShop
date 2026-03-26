package homework.javaspring_model.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Controller
public class HomeController {
    @Autowired
    private MessageSource messageSource;
    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserDetails userDetails, Model model, Locale locale) {
        String username = userDetails.getUsername();

        String greeting = messageSource.getMessage(
                "user.greeting",
                new Object[]{username},
                locale
        );

        model.addAttribute("welcomeMsg",
        messageSource.getMessage("welcome.message", null, locale));
        model.addAttribute("greeting", greeting);
        model.addAttribute("title",
        messageSource.getMessage("page.title", null, locale));

        return "index";
    }

    //test
    @GetMapping("/home")
    public String home() {
        return "redirect:/profile";
    }
}
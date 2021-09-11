package jslog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Navigation 이 제공하는 URL들을 매핑하는 Home Controller
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request) {
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/portfolio")
    public String portfolio() {
        return "portfolio/portfolio-home";
    }

    @GetMapping("/portfolio/JSLog")
    public String jslog() {
        return "portfolio/jslog";
    }

    @GetMapping("/portfolio/WantToSay")
    public String wantToSay() {
        return "portfolio/want-to-say";
    }

}

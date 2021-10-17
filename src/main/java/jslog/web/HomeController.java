package jslog.web;

import jslog.domain.member.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * Navigation 이 제공하는 URL들을 매핑하는 Home Controller
 */
@Controller
public class HomeController {

    @GetMapping("")
    public String home() {
        return "redirect:/blog";
    }

    @GetMapping("/about")
    public String about(Model model, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)Member member) {
        model.addAttribute("loginMember", member);
        return "about";
    }

    @GetMapping("/portfolio")
    public String portfolio(Model model, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)Member member) {
        model.addAttribute("loginMember", member);
        return "portfolio/portfolio-home";
    }

    @GetMapping("/portfolio/JSLog")
    public String jslog(Model model, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)Member member) {
        model.addAttribute("loginMember", member);
        return "portfolio/jslog";
    }

    @GetMapping("/portfolio/WantToSay")
    public String wantToSay(Model model, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)Member member) {
        model.addAttribute("loginMember", member);
        return "portfolio/want-to-say";
    }

}

package jslog;

import jslog.commons.SessionConst;
import jslog.member.auth.ui.LoginMember;
import jslog.member.member.domain.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * Navigation 이 제공하는 URL들을 매핑하는 Home Controller
 */
@Controller
public class HomeController {

    @GetMapping("")
    public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) LoginMember loginMember,
                       Model model) {
        model.addAttribute("loginMember", loginMember);
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member) {
        model.addAttribute("loginMember", member);
        return "about";
    }

    @GetMapping("/portfolio")
    public String portfolio(Model model, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member) {
        model.addAttribute("loginMember", member);
        return "portfolio/portfolio-home";
    }

    @GetMapping("/portfolio/JSLog")
    public String jslog(Model model, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member) {
        model.addAttribute("loginMember", member);
        return "portfolio/jslog";
    }

    @GetMapping("/portfolio/WantToSay")
    public String wantToSay(Model model, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member) {
        model.addAttribute("loginMember", member);
        return "portfolio/want-to-say";
    }

    @GetMapping("/portfolio/Popcat")
    public String popcat(Model model, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)Member member) {
        model.addAttribute("loginMember", member);
        return "portfolio/popcat";
    }

}

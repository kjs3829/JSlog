package jslog.web.login;

import jslog.domain.login.LoginService;
import jslog.domain.member.Member;
import jslog.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm loginForm) {
        return "sign-in";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm form, @RequestParam(required = false) String redirectURL, HttpServletRequest request) {
        Member loginMember = loginService.login(form.getEmail(), form.getPassword());
        log.info("login member = {}", loginMember);

        if(loginMember == null) {
            return "sign-in";
        }

        //로그인 성공
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        if (redirectURL == null) return "redirect:/";
        return "redirect:" + redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}

package com.jslog.blog.web.login;

import com.jslog.blog.domain.login.LoginService;
import com.jslog.blog.domain.member.Member;
import com.jslog.blog.web.SessionConst;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final LoginService loginService;

    //TODO : sign-in.html 수정
    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm loginForm) {
        return "sign-in";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm form, HttpServletRequest request) {
        Member loginMember = loginService.login(form.getEmail(), form.getPassword());
        log.info("login member = {}", loginMember);

        if(loginMember == null) {
            return "sign-in";
        }

        //로그인 성공
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return "redirect:/";
    }
}

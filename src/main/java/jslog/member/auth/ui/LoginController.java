package jslog.member.auth.ui;

import jslog.member.auth.application.LoginService;
import jslog.member.auth.infrastructure.kakao.KakaoOauthInfo;
import jslog.member.auth.domain.ProviderName;
import jslog.member.member.domain.Member;
import jslog.commons.SessionConst;
import jslog.member.auth.ui.dto.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final LoginService loginService;
    private final KakaoOauthInfo kakaoOauthInfo;

    @GetMapping("/login")
    public String loginForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) LoginMember loginMember,
                            @ModelAttribute LoginForm loginForm,
                            Model model) {
        String kakaoAuth = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+kakaoOauthInfo.getClientId()+"&redirect_uri="+kakaoOauthInfo.getKakaoRedirectUrl();
        model.addAttribute("kakaoAuth", kakaoAuth);
        model.addAttribute("loginMember", loginMember);
        return "sign-in";
    }

    @GetMapping("/auth/kakao/login")
    public String kakaoLogin(String code, HttpServletRequest request) {
        loginService.createSession(request, ProviderName.KAKAO,code);
        System.out.println(request.getSession().getAttribute(SessionConst.LOGIN_MEMBER));
        return "redirect:/";
    }

}

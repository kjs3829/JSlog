package jslog.web.login;

import jslog.domain.member.MemberMySQLRepository;
import jslog.domain.login.LoginService;
import jslog.domain.login.SignUpService;
import jslog.domain.member.entity.KakaoMember;
import jslog.domain.member.entity.Member;
import jslog.web.SessionConst;
import jslog.web.login.oauth.kakao.KakaoAuthToken;
import jslog.web.login.oauth.kakao.KakaoLoginAPI;
import jslog.web.login.oauth.kakao.KakaoProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final SignUpService signUpService;
    private final LoginService loginService;
    private final MemberMySQLRepository memberMySQLRepository;
    private final KakaoLoginAPI kakaoLoginAPI;
    private final String REDIRECT_URI ="http://localhost:8080/auth/kakao/login";

    @GetMapping("/login")
    public String loginForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member,
                            @ModelAttribute LoginForm loginForm,
                            Model model) {
        String REST_API_KEY="9b73f9c1ef9ccf7d60ad9de541112929";
        String kakaoAuth = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+REST_API_KEY+"&redirect_uri="+REDIRECT_URI;
        model.addAttribute("kakaoAuth", kakaoAuth);
        model.addAttribute("loginMember", member);
        return "sign-in";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm form, @RequestParam(required = false) String redirectURL, HttpServletRequest request) {
        Member loginMember = loginService.jslogLogin(form.getEmail(), form.getPassword());
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

    @GetMapping("/auth/kakao/login")
    public String kakaoLogin(String code, HttpServletRequest request, HttpServletResponse response) throws IOException {

        KakaoAuthToken kakaoAuthToken = kakaoLoginAPI.getAuthToken(code, REDIRECT_URI);
        KakaoProfile kakaoProfile = kakaoLoginAPI.getProfile(kakaoAuthToken);

        // 서비스 회원가입이 되어있을 경우 로그인 프로세스 진행
        if (kakaoProfile.getProperties().getSigned_up()) {
            String email = kakaoProfile.getKakao_account().getEmail();
            // 로그인 프로세스
            Member loginMember = memberMySQLRepository.findByEmail(email).orElse(null);
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        }

        // 서비스 회원가입이 되어있지 않을 경우 회원가입 프로세스 진행
        else {
            KakaoProfile.KakaoAccount kakaoAccount = kakaoProfile.getKakao_account();
            // 서비스 정책으로 사용자 정보를 동의하지 않으면 회원가입 진행이 불가능하게 하기로 함
            // 사용자 정보(email, nickname) 동의를 한 사용자만 회원가입 프로세스를 진행
            // 회원가입이 완료되면 로그인 프로세스 진행
            if (!kakaoAccount.getEmail_needs_agreement() && !kakaoAccount.getProfile_nickname_needs_agreement()) {

                // 회원가입 프로세스
                KakaoMember newKakaoMember = signUpService.kakaoSignUp(kakaoProfile, kakaoAuthToken);

                // 로그인 프로세스
                Member loginMember = memberMySQLRepository.findById(newKakaoMember.getId());
                HttpSession session = request.getSession();
                session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
            }

            // 사용자 정보 동의를 하지 않았을 경우 카카오계정의 연결을 끊고 회원가입 실패 화면으로 이동
            // 카카오계정의 연결을 끊지 않으면 사용자 정보 동의 창이 제공되지 않는다.
            else {
                kakaoLoginAPI.unLink(kakaoAuthToken);
                response.setContentType("text/html; charset=euc-kr");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('카카오 계정으로 로그인시 사용자 정보 제공 동의를 허락해야만 로그인을 진행할 수 있습니다.'); location.href='/login';</script>");
                out.flush();
                return "about"; // response를 두번 하지 않기 위해서 억지로 넣은 코드. 더 좋은 방법을 찾을 필요가 있다.
            }
        }
        return "redirect:/";
    }

}

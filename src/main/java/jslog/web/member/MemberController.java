package jslog.web.member;

import jslog.domain.member.*;
import jslog.domain.member.entity.JslogMember;
import jslog.domain.member.entity.KakaoMember;
import jslog.domain.member.entity.Member;
import jslog.domain.member.form.ProfileUpdateForm;
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

/*
    TODO:
       로그인 아이디 중복 검사 기능 개발
       Bean Validation 추가
*/

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {
    private final MemberMySQLRepository memberMySQLRepository;
    private final JslogMemberRepository jslogMemberRepository;
    private final KakaoMemberRepository kakaoMemberRepository;
    private final KakaoLoginAPI kakaoLoginAPI;

    @GetMapping("/jslog/signup")
    public String signupForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member,
                             @ModelAttribute AddMemberForm addMemberForm,
                             Model model) {
        model.addAttribute("loginMember", member);
        return "sign-up";
    }

    @PostMapping("/jslog/signup")
    public String signup(@ModelAttribute AddMemberForm addMemberForm) {
        log.info("Sign Up Member = {}", addMemberForm);

        JslogMember addMember = JslogMember.builder().email(addMemberForm.getEmail())
                .password(addMemberForm.getPassword())
                .nickname(addMemberForm.getNickname())
                .memberRole(MemberRole.MEMBER)
                .build();

        jslogMemberRepository.add(addMember);
        return "redirect:/";
    }

    // member테이블 cascade설정 안함. 즉, 현재 게시글이 존재하는 멤버는 탈퇴가 불가능함.
    @DeleteMapping("/jslog")
    public String deleteMember(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member, HttpServletRequest request) {
        JslogMember deleteMember = jslogMemberRepository.findById(member.getId());
        jslogMemberRepository.delete(deleteMember);
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/setting")
    public String settingMemberForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
                                    Model model) {
        ProfileUpdateForm profileUpdateForm = member.getProfileUpdateForm();
        model.addAttribute("profileUpdateForm", profileUpdateForm);
        model.addAttribute("loginMember", member);
        if (member.getDtype().equals("JslogMember")) {
            return "jslog-member-setting";
        } else if (member.getDtype().equals("KakaoMember")) {
            String REST_API_KEY="9b73f9c1ef9ccf7d60ad9de541112929";
            String REDIRECT_URI ="http://localhost:8080/members/kakao/delete";
            String kakaoAuth = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+REST_API_KEY+"&redirect_uri="+REDIRECT_URI;
            model.addAttribute("kakaoAuth", kakaoAuth);
            return "kakao-member-setting";
        }

        //ERROR
        return "redirect:/";
    }

    @PostMapping("/setting")
    public String updateMemberProfile(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member member,
                                      @ModelAttribute ProfileUpdateForm profileUpdateForm,
                                      HttpServletRequest request) {
        System.out.println("profileUpdateForm = " + profileUpdateForm.getNickname());
        Member updateMember = memberMySQLRepository.updateProfile(member, profileUpdateForm);

        //세션 재발급
        HttpSession session = request.getSession(false);
        session.setAttribute(SessionConst.LOGIN_MEMBER, updateMember);
        return "redirect:/blog";
    }

    // member테이블 cascade설정 안함. 즉, 현재 게시글이 존재하는 멤버는 탈퇴가 불가능함.
    @GetMapping("/kakao/delete")
    public String kakaoDelete(String code, HttpServletRequest request) {
        String REDIRECT_URI ="http://localhost:8080/members/kakao/delete";
        KakaoAuthToken authToken = kakaoLoginAPI.getAuthToken(code, REDIRECT_URI);
        kakaoLoginAPI.unLink(authToken);

        HttpSession session = request.getSession(false);
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        KakaoMember deleteMember = kakaoMemberRepository.findById(member.getId());
        kakaoMemberRepository.delete(deleteMember);
        session.invalidate();

        return "redirect:/";
    }

}

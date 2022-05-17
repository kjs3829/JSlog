package jslog.member.member.ui;

import jslog.member.auth.ui.LoginMember;
import jslog.member.member.application.MemberService;
import jslog.member.member.ui.dto.ProfileUpdateRequest;
import jslog.commons.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    private final MemberService memberService;

    @GetMapping("/setting")
    public String settingMemberForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                                    Model model) {

        model.addAttribute("profileUpdateRequest", new ProfileUpdateRequest(loginMember.getNickname()));
        model.addAttribute("loginMember", loginMember);

        return "member-setting";
    }

    @PostMapping("/setting")
    public String updateMemberProfile(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                                      @ModelAttribute ProfileUpdateRequest profileUpdateRequest,
                                      HttpSession session) {

        memberService.updateMember(loginMember, profileUpdateRequest, session);

        return "redirect:/members/setting";
    }

    // member테이블 cascade설정 안함. 즉, 현재 게시글이 존재하는 멤버는 탈퇴가 불가능함.
    @GetMapping("/kakao/delete")
    public String kakaoDelete(String code, HttpServletRequest request) {
        String REDIRECT_URI ="http://localhost:8080/members/kakao/delete";
        /*
        KakaoAuthToken authToken = kakaoLoginAPI.getAuthToken(code, REDIRECT_URI);
        kakaoLoginAPI.unLink(authToken);

        HttpSession session = request.getSession(false);
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        KakaoMember deleteMember = kakaoMemberRepository.findById(member.getId());
        kakaoMemberRepository.delete(deleteMember);
        session.invalidate();*/

        return "redirect:/";
    }

}

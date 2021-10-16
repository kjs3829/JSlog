package jslog.web.member;

import jslog.domain.member.JslogMemberRepository;
import jslog.domain.member.MemberMySQLRepository;
import jslog.domain.member.entity.JslogMember;
import jslog.domain.member.MemberRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    private final MemberMySQLRepository memberRepository;
    private final JslogMemberRepository jslogMemberRepository;

    @GetMapping("/jslog/signup")
    public String signupForm(@ModelAttribute AddMemberForm addMemberForm) {
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

    @DeleteMapping("/jslog/{id}")
    public String deleteMember(@PathVariable(name = "id") Long id) {
        JslogMember deleteMember = jslogMemberRepository.findById(id);
        jslogMemberRepository.delete(deleteMember);
        return "redirect:/";
    }

}

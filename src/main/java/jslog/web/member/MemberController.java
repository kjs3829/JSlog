package jslog.web.member;

import jslog.domain.MemberMySQLRepository;
import jslog.domain.member.Member;
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

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute AddMemberForm addMemberForm) {
        return "sign-up";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute AddMemberForm addMemberForm) {
        log.info("Sign Up Member = {}", addMemberForm);
        Member addMember = new Member.Builder().setEmail(addMemberForm.getEmail())
                .setPassword(addMemberForm.getPassword())
                .setNickname(addMemberForm.getNickname())
                .setMemberRole(MemberRole.MEMBER)
                .build();
        memberRepository.add(addMember);
        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String deleteMember(@PathVariable(name = "id") Long id) {
        memberRepository.delete(id);
        return "redirect:/";
    }

}

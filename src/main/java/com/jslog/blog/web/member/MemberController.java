package com.jslog.blog.web.member;

import com.jslog.blog.domain.MemberMySQLRepository;
import com.jslog.blog.domain.MemberRepository;
import com.jslog.blog.domain.member.Member;
import com.jslog.blog.domain.member.MemberRole;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {
    private MemberMySQLRepository memberRepository;

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

}

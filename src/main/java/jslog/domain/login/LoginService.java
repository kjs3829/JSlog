package jslog.domain.login;

import jslog.domain.member.JslogMemberRepository;
import jslog.domain.member.entity.JslogMember;
import jslog.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final JslogMemberRepository jslogMemberRepository;

    public Member jslogLogin(String email, String password) {
        JslogMember loginMember = jslogMemberRepository.findByEmail(email);
        if (loginMember == null || !loginMember.getPassword().equals(password)) return null;
        return loginMember;
    }

}

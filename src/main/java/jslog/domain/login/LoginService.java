package jslog.domain.login;

import jslog.domain.MemberMySQLRepository;
import jslog.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberMySQLRepository memberMySQLRepository;

    public Member login(String email, String password) {
        return memberMySQLRepository.findByEmail(email)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}

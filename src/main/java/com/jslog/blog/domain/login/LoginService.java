package com.jslog.blog.domain.login;

import com.jslog.blog.domain.MemberMySQLRepository;
import com.jslog.blog.domain.member.Member;
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

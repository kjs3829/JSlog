package jslog.member.member.application;

import jslog.member.auth.application.LoginStrategyFactory;
import jslog.member.auth.ui.LoginMember;
import jslog.member.member.domain.Member;
import jslog.member.member.ui.dto.ProfileUpdateRequest;
import jslog.member.member.repository.MemberRepository;
import jslog.commons.SessionConst;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpSession;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class MemberService {
    private final LoginStrategyFactory loginStrategyFactory;
    private final MemberRepository memberRepository;

    @Transactional
    public void updateMember(LoginMember loginMember, ProfileUpdateRequest profileUpdateRequest, HttpSession session) {
        Member updateMember = memberRepository.findById(loginMember.getId()).orElseThrow(NoSuchElementException::new);
        updateMember.updateProfile(profileUpdateRequest);

        session.setAttribute(SessionConst.LOGIN_MEMBER, LoginMember.createMember(updateMember));
    }

}

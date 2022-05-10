package jslog.member.member.application;

import jslog.member.auth.application.Client;
import jslog.member.auth.application.LoginStrategyFactory;
import jslog.member.auth.ui.LoginMember;
import jslog.member.member.domain.Member;
import jslog.member.member.ui.dto.ProfileUpdateForm;
import jslog.member.member.repository.MemberRepository;
import jslog.commons.SessionConst;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
@AllArgsConstructor
public class MemberService {
    private final LoginStrategyFactory loginStrategyFactory;
    private final MemberRepository memberRepository;

    @Transactional
    public void updateMember(LoginMember loginMember, ProfileUpdateForm profileUpdateForm, HttpServletRequest request) {
        Member updateMember = memberRepository.findById(loginMember.getId()).orElseGet(null);
        updateMember.updateProfile(profileUpdateForm);

        HttpSession session = request.getSession(false);
        session.setAttribute(SessionConst.LOGIN_MEMBER, LoginMember.createMember(updateMember));
    }


    public void withdrawal(Member member) {
        Client client = loginStrategyFactory.selectClient(member.getProvider().getProviderName());

    }
}

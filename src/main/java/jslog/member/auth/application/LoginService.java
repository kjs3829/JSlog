package jslog.member.auth.application;

import jslog.member.auth.ui.LoginMember;
import jslog.member.member.domain.Member;
import jslog.member.member.repository.MemberRepository;
import jslog.member.member.domain.MemberRole;
import jslog.member.auth.domain.Provider;
import jslog.member.auth.domain.ProviderName;
import jslog.commons.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final LoginStrategyFactory loginStrategyFactory;
    private final MemberRepository memberRepository;

    @Transactional
    public void createSession(HttpServletRequest request, ProviderName providerName, String socialCode) {
        Client client = loginStrategyFactory.selectClient(providerName);
        MemberDetails details = client.getDetails(socialCode);

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, LoginMember.createMember(findOrCreateMember(details)));
    }

    private Member findOrCreateMember(MemberDetails memberDetails) {
        return memberRepository.findByProvideIdAndProviderName(memberDetails.accountId(), memberDetails.providerCode()).orElseGet(() -> {
            final Provider provider = Provider.create(memberDetails.accountId(), memberDetails.providerCode());
            return memberRepository.save(createMember(provider));
        });


    }

    private Member createMember(Provider provider) {
        return Member.create(provider, "사용자1",MemberRole.MEMBER);
    }

}

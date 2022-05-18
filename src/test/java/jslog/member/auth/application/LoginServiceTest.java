package jslog.member.auth.application;

import jslog.member.auth.domain.ProviderName;
import jslog.member.auth.infrastructure.kakao.KakaoMemberDetails;
import jslog.member.member.domain.Member;
import jslog.member.member.domain.MemberRole;
import jslog.member.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock LoginStrategyFactory loginStrategyFactory;
    @Mock MemberRepository memberRepository;
    @Mock Client client;
    @Mock HttpServletRequest request;
    @Mock HttpSession session;
    LoginService loginService;
    Member member;

    @BeforeEach
    void setUp() {
        loginService = new LoginService(loginStrategyFactory, memberRepository);

        member = Member.builder().id(1L).nickname("테스트 유저").memberRole(MemberRole.MEMBER).build();
    }

    @Test
    @DisplayName("회원의 세션 생성 성공")
    void createSession_member() {
        when(loginStrategyFactory.selectClient(any())).thenReturn(client);
        when(client.getDetails(any())).thenReturn(new KakaoMemberDetails("1"));
        when(request.getSession()).thenReturn(session);
        when(memberRepository.findByProvideIdAndProviderName(any(),any())).thenReturn(Optional.of(member));

        loginService.createSession(request, ProviderName.KAKAO, "socialCode");

        verify(session).setAttribute(anyString(),any());
    }

    @Test
    @DisplayName("비회원의 세션 생성 성공")
    void createSession_anonymous() {
        when(loginStrategyFactory.selectClient(any())).thenReturn(client);
        when(client.getDetails(any())).thenReturn(new KakaoMemberDetails("1"));
        when(request.getSession()).thenReturn(session);
        when(memberRepository.findByProvideIdAndProviderName(any(),any())).thenReturn(Optional.empty());
        when(memberRepository.save(any())).thenReturn(member);

        loginService.createSession(request, ProviderName.KAKAO, "socialCode");

        verify(session).setAttribute(anyString(),any());
    }

}
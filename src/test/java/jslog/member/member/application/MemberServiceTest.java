package jslog.member.member.application;


import jslog.member.auth.application.LoginStrategyFactory;
import jslog.member.auth.ui.LoginMember;
import jslog.member.member.domain.Member;
import jslog.member.member.repository.MemberRepository;
import jslog.member.member.ui.dto.ProfileUpdateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock LoginStrategyFactory loginStrategyFactory;

    @Mock MemberRepository memberRepository;

    @Mock HttpSession session;

    MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(loginStrategyFactory, memberRepository);
    }

    @Test
    @DisplayName("회원 정보 수정에 성공한다.")
    void updateMember() {
        //given
        Member member = Member.builder()
                .id(1L)
                .nickname("변경 전 닉네임")
                .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        LoginMember loginMember = LoginMember.createMember(member);

        ProfileUpdateRequest profileUpdateRequest = ProfileUpdateRequest.builder().nickname("변경 후 닉네임").build();

        doNothing().when(session).setAttribute(anyString(),any());

        //when
        memberService.updateMember(loginMember, profileUpdateRequest, session);

        //then
        Assertions.assertThat(member.getNickname()).isEqualTo("변경 후 닉네임");
    }
}

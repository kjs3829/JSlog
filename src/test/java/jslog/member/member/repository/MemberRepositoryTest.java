package jslog.member.member.repository;

import jslog.member.auth.domain.Provider;
import jslog.member.auth.domain.ProviderName;
import jslog.member.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("ProvideId 와 ProviderName 으로 회원 조회")
    void findByProvideIdTest() {
        //given
        Provider provider = Provider.create("TestUser", ProviderName.KAKAO);
        Member member = Member.create(provider, null, null);
        memberRepository.save(member);

        //when
        Member findMember = memberRepository.findByProvideIdAndProviderName(provider.getProvideId(), provider.getProviderName())
                .orElseThrow(NoSuchElementException::new);

        //then
        assertThat(member.getId()).isEqualTo(findMember.getId());
    }
}
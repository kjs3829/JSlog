package jslog.domain.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import jslog.domain.member.KakaoMemberRepository;
import jslog.domain.member.entity.KakaoMember;
import jslog.domain.member.MemberRole;
import jslog.web.login.oauth.kakao.KakaoAuthToken;
import jslog.web.login.oauth.kakao.KakaoLoginAPI;
import jslog.web.login.oauth.kakao.KakaoProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpService {
    private final KakaoMemberRepository kakaoMemberRepository;
    private final KakaoLoginAPI kakaoLoginAPI;

    // 카카오 계정으로 회원가입시 서비스 회원가입 여부 카카오 사용자 프로퍼티를 true로 전환시켜주는 작업과
    // DB에 신규 사용자 정보 등록 작업 수행
    @Transactional
    public KakaoMember kakaoSignUp(KakaoProfile kakaoProfile, KakaoAuthToken kakaoAuthToken) throws JsonProcessingException {
        System.out.println("kakao NickName is = "+kakaoProfile.getProperties().getNickname());
        KakaoProfile.KakaoAccount kakaoAccount = kakaoProfile.getKakao_account();
        KakaoMember newKakaoMember = KakaoMember.builder().nickname(kakaoAccount.getProfile().getNickname())
                .email(kakaoAccount.getEmail())
                .memberRole(MemberRole.MEMBER)
                .kakaoId(kakaoProfile.getId())
                .build();

        KakaoProfile.Properties properties = kakaoProfile.getProperties();
        properties.setSigned_up(true);
        kakaoLoginAPI.updateProperty(kakaoAuthToken, properties);
        kakaoMemberRepository.add(newKakaoMember);
        newKakaoMember.setDtype("KakaoMember"); // 최초 회원가입시 @discriminator 컬럼인 member의 dtype이 제대로 반영되지 않아 이런 방법을 채택.
        return newKakaoMember;
    }
}

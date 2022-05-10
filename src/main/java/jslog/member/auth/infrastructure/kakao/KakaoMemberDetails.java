package jslog.member.auth.infrastructure.kakao;

import jslog.member.auth.application.MemberDetails;
import jslog.member.auth.domain.ProviderName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor
public class KakaoMemberDetails implements MemberDetails {

    private String id;

    @Override
    public String accountId() {
        return id;
    }

    @Override
    public ProviderName providerCode() {
        return ProviderName.KAKAO;
    }
}

package jslog.domain.member.entity;

import jslog.domain.member.MemberRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity @NoArgsConstructor @Getter
@Inheritance(strategy = InheritanceType.JOINED)
public class KakaoMember extends Member{
    private Long kakaoId;

    @Builder
    public KakaoMember(String email, String nickname, MemberRole memberRole, Long kakaoId) {
        super(email, nickname, memberRole);
        this.kakaoId = kakaoId;
    }
}

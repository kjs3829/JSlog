package jslog.member.member.domain;

import jslog.member.auth.domain.Provider;
import jslog.member.member.ui.dto.ProfileUpdateRequest;
import jslog.commons.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Embedded
    private Provider provider;

    private String nickname;
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Builder
    public Member(Long id, Provider provider, String nickname, MemberRole memberRole) {
        this.id = id;
        this.provider = provider;
        this.nickname = nickname;
        this.memberRole = memberRole;
    }

    public static Member create(Provider provider, String nickname, MemberRole memberRole) {
        return new Member(null, provider, nickname, memberRole);
    }

    public ProfileUpdateRequest getProfileUpdateForm() {
        return new ProfileUpdateRequest(this.getNickname());
    }

    public void updateProfile(ProfileUpdateRequest profileUpdateRequest) {
        this.nickname = profileUpdateRequest.getNickname();
    }
}

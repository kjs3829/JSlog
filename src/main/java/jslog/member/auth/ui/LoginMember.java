package jslog.member.auth.ui;

import jslog.member.member.domain.Member;
import jslog.member.member.domain.MemberRole;
import jslog.member.member.ui.dto.ProfileUpdateRequest;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class LoginMember {
    Long id;
    String nickname;
    MemberRole memberRole;

    public static LoginMember createMember(Member member) {
        return new LoginMember(member.getId(), member.getNickname(), member.getMemberRole());
    }

    public static LoginMember createMember(Long id, String nickname, MemberRole memberRole) {
        return new LoginMember(id, nickname, memberRole);
    }

    public ProfileUpdateRequest getProfileUpdateForm() {
        return new ProfileUpdateRequest(this.getNickname());
    }
}

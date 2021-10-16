package jslog.domain.member;

import jslog.domain.member.entity.Member;
import lombok.Data;

@Data
public class MemberToString {
    private Long id;
    private String email;
    private String nickname;
    private MemberRole memberRole;

    public MemberToString(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.memberRole = member.getMemberRole();
    }
}

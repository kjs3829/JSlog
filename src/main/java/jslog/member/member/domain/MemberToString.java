package jslog.member.member.domain;

import lombok.Data;

@Data
public class MemberToString {
    private Long id;
    private String nickname;
    private MemberRole memberRole;

    public MemberToString(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.memberRole = member.getMemberRole();
    }
}

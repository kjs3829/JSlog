package com.jslog.blog.domain.member;

import lombok.Data;

@Data
public class MemberToString {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private MemberRole memberRole;

    public MemberToString(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.nickname = member.getNickname();
        this.memberRole = member.getMemberRole();
    }
}

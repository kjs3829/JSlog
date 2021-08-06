package com.jslog.blog.domain.member;

import com.jslog.blog.domain.post.Post;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @ToString
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String password;
    private String nickname;
    private MemberRole memberRole;

    @OneToMany(mappedBy = "author")
    private List<Post> posts = new ArrayList<>();

    public Member() {

    }

    public Member(Builder builder) {
        this.email = builder.email;
        this.password = builder.password;
        this.nickname = builder.nickname;
        this.memberRole = builder.memberRole;
    }

    public static class Builder {
        private String email;
        private String password;
        private String nickname;
        private MemberRole memberRole;

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setNickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder setMemberRole(MemberRole memberRole) {
            this.memberRole = memberRole;
            return this;
        }

        public Member build() {
            return new Member(this);
        }

    }
}

package jslog.domain.member;

import jslog.domain.post.entity.Post;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String password;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @OneToMany(mappedBy = "author")
    private List<Post> posts = new ArrayList<>();

    public Member() {

    }

    public Member(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.password = builder.password;
        this.nickname = builder.nickname;
        this.memberRole = builder.memberRole;
    }

    public static class Builder {
        private Long id;
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

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Member build() {
            return new Member(this);
        }

    }

    // Member의 posts는 로그에서 제거
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(new MemberToString(this), ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

package jslog.domain.member.entity;

import jslog.domain.member.MemberRole;
import jslog.domain.member.MemberToString;
import jslog.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@DiscriminatorColumn @NoArgsConstructor
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @OneToMany(mappedBy = "author")
    private List<Post> posts = new ArrayList<>();

    public Member(String email, String nickname, MemberRole memberRole) {
        this.email = email;
        this.nickname = nickname;
        this.memberRole = memberRole;
    }

    // Member의 posts는 로그에서 제거
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(new MemberToString(this), ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

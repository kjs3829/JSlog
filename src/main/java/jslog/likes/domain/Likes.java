package jslog.likes.domain;

import jslog.member.member.domain.Member;
import jslog.post.domain.Post;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Likes {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public static Likes create(Member member, Post post) {
        return new Likes(member, post);
    }

    private Likes(Member member, Post post) {
        this.member = member;
        this.post = post;
    }

    @Builder
    public Likes(Long id, Member member, Post post) {
        this.id = id;
        this.member = member;
        this.post = post;
    }

}

package jslog.comment.domain;

import jslog.comment.ui.dto.CommentEditForm;
import jslog.commons.domain.BaseEntity;
import jslog.member.member.domain.Member;
import jslog.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    private String content;

    public static Comment create(Post post, Member author, String content) {
        return new Comment(post, author, content);
    }

    public void edit(CommentEditForm commentEditForm) {
        content = commentEditForm.getContent();
    }

    private Comment(Post post, Member author, String content) {
        this.post = post;
        this.author = author;
        this.content = content;
    }
}

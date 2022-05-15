package jslog.postWithTag.domain;

import jslog.post.domain.Post;
import jslog.tag.domain.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter @NoArgsConstructor
public class PostWithTag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_with_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public PostWithTag(Post post, Tag tag) {
        setPost(post);
        this.tag = tag;
    }

    private void setPost(Post post) {
        if (this.post != null) {
            this.post.getPostWithTags().remove(this);
        }
        this.post = post;
        post.getPostWithTags().add(this);
    }
}

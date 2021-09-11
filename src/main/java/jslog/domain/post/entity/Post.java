package jslog.domain.post.entity;

import jslog.domain.member.Member;
import jslog.domain.post.BaseTimeEntity;
import jslog.domain.post.PostToString;
import jslog.domain.post.form.PostEditForm;
import jslog.domain.post.form.PostSelectForm;
import jslog.domain.post.form.PostWriteForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Member author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostWithTag> postWithTags = new ArrayList<>();

    private String title;
    private String content;
    private String url;
    private String preview;

    public Post(Builder builder) {
        this.id = builder.id;
        this.author = builder.author;
        this.title = builder.title;
        this.content = builder.content;
        this.url = builder.url;
        this.preview = builder.preview;
    }

    public Post(PostWriteForm postWriteForm) {
        this.author = postWriteForm.getAuthor();
        this.title = postWriteForm.getTitle();
        this.content = postWriteForm.getContent();
        this.url = postWriteForm.getUrl();
    }

    public PostSelectForm toSelectForm() {
        return new PostSelectForm(this);
    }

    public void edit(PostEditForm postEditForm) {
        this.title = postEditForm.getTitle();
        this.content = postEditForm.getContent();
        this.url = postEditForm.getUrl();
        this.preview = postEditForm.getPreview();
    }

    // 로그에 찍히는 content 길이 최대 40글자
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(new PostToString(this), ToStringStyle.MULTI_LINE_STYLE);
    }

    public List<Tag> getTags() {
        List<Tag> tags = new ArrayList<Tag>();
        if (postWithTags != null) {
            for (PostWithTag pwt : this.postWithTags) {
                tags.add(pwt.getTag());
            }
        }
        return tags;
    }

    public static class Builder {
        private Long id;
        private Member author;
        private String title;
        private String content;
        private String url;
        private String preview;

        public Builder setAuthor(Member author) {
            this.author = author;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setPreview(String preview) {
            this.preview = preview;
            return this;
        }

        public Post build() {
            return new Post(this);
        }

    }
}
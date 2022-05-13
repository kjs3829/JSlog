package jslog.post.domain;

import jslog.comment.domain.Comment;
import jslog.member.member.domain.Member;
import jslog.commons.domain.BaseEntity;
import jslog.post.domain.url.CustomUrl;
import jslog.post.ui.dto.PostToString;
import jslog.post.ui.dto.PostEditForm;
import jslog.postWithTag.domain.PostWithTag;
import jslog.tag.domain.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @OneToMany(mappedBy = "post")
    private List<PostWithTag> postWithTags = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    private String title;
    @Lob private String content;
    @Embedded private CustomUrl customUrl;
    private String preview;
    @Setter private Long beforePostId;
    @Setter private Long nextPostId;
    private int hit;

    @Builder
    public Post (Member author, String title, String content, CustomUrl customUrl, String preview, Long beforePostId, int hit) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.customUrl = customUrl;
        this.preview = preview;
        this.beforePostId = beforePostId;
        this.hit = hit;
    }

    public boolean hasBeforePost() {
        return beforePostId != null;
    }
    public boolean hasNextPost() {
        return nextPostId != null;
    }

    public void edit(PostEditForm postEditForm) {
        this.title = postEditForm.getTitle();
        this.content = postEditForm.getContent();
        this.customUrl = CustomUrl.create(postEditForm.getUrl());
        this.preview = postEditForm.getPreview();
    }

    // 로그에 찍히는 content 길이 최대 40글자
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(new PostToString(this), ToStringStyle.MULTI_LINE_STYLE);
    }

    public List<Tag> getTags() {
        List<Tag> tags = new ArrayList<>();
        if (postWithTags != null) {
            for (PostWithTag pwt : this.postWithTags) {
                tags.add(pwt.getTag());
            }
        }
        return tags;
    }

    public String getStringTags() {
        StringBuilder stringTags = new StringBuilder();
        List<Tag> tags = getTags();
        stringTags.append(tags.get(0).getName());
        for (int i=1; i<tags.size(); i++) {
            stringTags.append(", ").append(tags.get(i).getName());
        }

        return stringTags.toString();
    }

    public String getStringUrl() {
        return customUrl.getUrl();
    }
}

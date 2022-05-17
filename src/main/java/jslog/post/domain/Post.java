package jslog.post.domain;

import jslog.comment.domain.Comment;
import jslog.member.member.domain.Member;
import jslog.commons.domain.BaseEntity;
import jslog.post.domain.url.CustomUrl;
import jslog.post.ui.dto.PostEditRequest;
import jslog.postWithTag.domain.PostWithTag;
import jslog.tag.domain.Tag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

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
    public Post (Long id, Member author, String title, String content, CustomUrl customUrl, String preview, Long beforePostId, int hit) {
        this.id = id;
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

    public void edit(PostEditRequest postEditRequest) {
        this.title = postEditRequest.getTitle();
        this.content = postEditRequest.getContent();
        this.customUrl = CustomUrl.create(postEditRequest.getUrl());
        this.preview = postEditRequest.getPreview();
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
        if (!tags.isEmpty()) stringTags.append(tags.get(0).getName());
        for (int i=1; i<tags.size(); i++) {
            stringTags.append(", ").append(tags.get(i).getName());
        }

        return stringTags.toString();
    }

    public String getStringUrl() {
        return customUrl.getUrl();
    }

    /**
     * markdown으로 작성된 게시글의 내용을 html 형식으로 바꾼다.
     */
    public String getRenderedContent() {
        if (this.content == null) return "";
        Parser parser = Parser.builder().build();
        Node document = parser.parse(this.content);
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        return renderer.render(document);
    }
}

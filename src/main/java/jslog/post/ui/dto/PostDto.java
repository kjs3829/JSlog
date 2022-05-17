package jslog.post.ui.dto;

import jslog.member.member.domain.Member;
import jslog.post.domain.Post;
import jslog.tag.domain.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor
public class PostDto {
    private Long id;
    private Member author;
    private String title;
    private String content;
    private String url;
    private List<Tag> tags;
    private String stringTags;
    private String createdDate;
    private Long beforePostId;
    private Long nextPostId;
    private String preview;

    private PostDto(Post post) {
        this.id = post.getId();
        this.author = post.getAuthor();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.url = post.getStringUrl();
        this.tags = post.getTags();
        this.stringTags = post.getStringTags();
        this.createdDate = post.getCreatedDateYYYYMMDD();
        this.beforePostId = post.getBeforePostId();
        this.nextPostId = post.getNextPostId();
        this.preview = post.getPreview();
    }

    public static PostDto create(Post post) {
        return new PostDto(post);
    }
}

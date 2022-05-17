package jslog.post.ui.dto;

import jslog.member.member.domain.Member;
import jslog.tag.domain.Tag;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter @NoArgsConstructor @EqualsAndHashCode
public class PostResponse {
    private Long id;
    private Member author;
    private String title;
    private String content;
    private String url;
    private List<Tag> tags;
    private String createdDate;

    @Builder
    public PostResponse(Long id, Member author, String title, String content, String url, List<Tag> tags, String createdDate) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.url = url;
        this.tags = tags;
        this.createdDate = createdDate;
    }

    private PostResponse(PostDto postDto) {
        this.id = postDto.getId();
        this.author = postDto.getAuthor();
        this.title = postDto.getTitle();
        this.content = postDto.getContent();
        this.url = postDto.getUrl();
        this.tags = postDto.getTags();
        this.createdDate = postDto.getCreatedDate();
    }

    public static PostResponse create(PostDto postDto) {
        return new PostResponse(postDto);
    }
}

package jslog.post.ui.dto;

import jslog.member.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter @Setter @ToString
public class PostEditRequest {
    private Long id;
    private Member author;
    private String title;
    private String content;
    private String tags;
    @NotEmpty private String url;
    private String preview;


    @Builder
    public PostEditRequest(Long id, Member author, String title, String content, String tags, String url, String preview) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.url = url;
        this.preview = preview;
    }
}

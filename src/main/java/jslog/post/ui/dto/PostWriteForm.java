package jslog.post.ui.dto;

import jslog.member.member.domain.Member;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
public class PostWriteForm {
    private String title;
    private String content;
    @NotEmpty
    private String url;
    private String tags;
    private String preview;

    @Builder
    public PostWriteForm(String title, String content, @NotEmpty String url, String tags, String preview) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.tags = tags;
        this.preview = preview;
    }
}

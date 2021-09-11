package jslog.domain.post.form;

import jslog.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter @Setter @ToString
public class PostEditForm {
    private Long id;
    private Member author;
    private String title;
    private String content;
    @NotEmpty
    private String url;
    private String preview;
}

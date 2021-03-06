package jslog.domain.post.form;

import jslog.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter @Setter @ToString
public class PostWriteForm {
    private Member author;
    private String title;
    private String content;
    @NotEmpty
    private String url;
    private String tags;
    private String preview;

}

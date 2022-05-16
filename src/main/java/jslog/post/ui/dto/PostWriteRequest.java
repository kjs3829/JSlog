package jslog.post.ui.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PostWriteRequest {
    private String title;
    private String content;
    @NotEmpty private String url;
    private String tags;
    private String preview;

}

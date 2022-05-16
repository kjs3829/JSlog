package jslog.post.ui.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @EqualsAndHashCode
public class PrevNextPostResponse {
    private String url;
    private String title;
    private Long authorId;

    private PrevNextPostResponse(PostDto postDto) {
        this.title = postDto.getTitle();
        this.url = postDto.getUrl();
        this.authorId = postDto.getAuthor().getId();
    }

    public static PrevNextPostResponse create(PostDto postDto) {
        return new PrevNextPostResponse(postDto);
    }
}

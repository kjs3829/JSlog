package jslog.post.ui.dto;

import jslog.post.domain.Post;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @EqualsAndHashCode
public class PrevNextPostResponse {
    private String url;
    private String title;
    private Long authorId;

    private PrevNextPostResponse(Post post) {
        this.title = post.getTitle();
        this.url = post.getCustomUrl().getUrl();
        this.authorId = post.getAuthor().getId();
    }

    public static PrevNextPostResponse create(Post post) {
        return new PrevNextPostResponse(post);
    }
}

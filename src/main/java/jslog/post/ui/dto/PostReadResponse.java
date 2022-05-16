package jslog.post.ui.dto;

import jslog.comment.ui.dto.CommentResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @EqualsAndHashCode
public class PostReadResponse {
    private PostResponse postResponse;
    private PrevNextPostResponse prevPostResponse;
    private PrevNextPostResponse nextPostResponse;
    private LikesResponse likesResponse;
    private CommentResponse commentResponse;

    public PostReadResponse(PostResponse postResponse, PrevNextPostResponse prevPostResponse, PrevNextPostResponse nextPostResponse,
                            CommentResponse commentResponse, LikesResponse likesResponse) {
        this.postResponse = postResponse;
        this.prevPostResponse = prevPostResponse;
        this.nextPostResponse = nextPostResponse;
        this.commentResponse = commentResponse;
        this.likesResponse = likesResponse;
    }

    public static PostReadResponse create(PostResponse postResponse, PrevNextPostResponse prevNextPostResponse, PrevNextPostResponse nextPostResponse,
                                          CommentResponse commentResponse, LikesResponse likesResponse) {
        return new PostReadResponse(postResponse, prevNextPostResponse, nextPostResponse, commentResponse, likesResponse);
    }

}

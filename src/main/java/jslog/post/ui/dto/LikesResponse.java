package jslog.post.ui.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class LikesResponse {
    private int likes;
    private boolean login;
    private boolean liked;

    public static LikesResponse create(int likes, boolean login, boolean liked) {
        return new LikesResponse(likes, login, liked);
    }
}

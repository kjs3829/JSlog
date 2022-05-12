package jslog.post.domain.url;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomUrl {
    private final static int URL_MAX_LENGTH = 255;

    private String url;

    public static CustomUrl create(String url) {
        String customUrl = url.replaceAll(" ", "_")
                .replaceAll("[^ㄱ-ㅣ가-힣a-zA-Z0-9_-]", "");
        return new CustomUrl(customUrl.substring(0,Math.min(customUrl.length(),URL_MAX_LENGTH)));
    }

}

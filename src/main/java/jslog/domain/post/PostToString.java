package jslog.domain.post;

import jslog.domain.member.entity.Member;
import jslog.domain.post.entity.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostToString {
    private Long id;
    private Member author;
    private String title;
    private String content;
    private String url;
    private LocalDateTime created_time;
    private LocalDateTime modified_time;

    public PostToString(Post post) {
        this.id = post.getId();
        this.author = post.getAuthor();
        this.title = post.getTitle();
        int length = post.getContent().length();
        if (length >= 40) length = 40;
        this.content = post.getContent().substring(0,length);
        this.url = post.getUrl();
        this.created_time = post.getCreatedDate();
        this.modified_time = post.getModifiedDate();
    }
}

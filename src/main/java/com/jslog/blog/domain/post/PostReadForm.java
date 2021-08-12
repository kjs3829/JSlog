package com.jslog.blog.domain.post;

import com.jslog.blog.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class PostReadForm {
    private Long id;
    private Member author;
    private String title;
    private String content;
    private String url;

    public PostReadForm(Post post) {
        this.id = post.getId();
        this.author = post.getAuthor();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.url = post.getUrl();
    }
}

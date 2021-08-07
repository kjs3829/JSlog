package com.jslog.blog.domain.post;

import com.jslog.blog.domain.member.Member;
import lombok.Data;

@Data
public class PostToString {
    private Long id;
    private Member author;
    private String title;
    private String content;

    public PostToString(Post post) {
        this.id = post.getId();
        this.author = post.getAuthor();
        this.title = post.getTitle();
        int length = post.getContent().length();
        if (length >= 40) length = 40;
        this.content = post.getContent().substring(0,length);
    }
}

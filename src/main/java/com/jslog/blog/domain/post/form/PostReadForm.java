package com.jslog.blog.domain.post.form;

import com.jslog.blog.domain.member.Member;
import com.jslog.blog.domain.post.entity.Post;
import com.jslog.blog.domain.post.entity.PostWithTag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @ToString
public class PostReadForm {
    private Long id;
    private Member author;
    private String title;
    private String content;
    private String url;
    private List<String> tags;

    public PostReadForm(Post post) {
        this.id = post.getId();
        this.author = post.getAuthor();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.url = post.getUrl();
        this.tags = post.getTags();
    }
}

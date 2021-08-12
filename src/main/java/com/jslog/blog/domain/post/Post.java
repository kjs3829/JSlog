package com.jslog.blog.domain.post;

import com.jslog.blog.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Member author;

    private String title;
    private String content;
    private String url;

    public Post(PostWriteForm postWriteForm) {
        this.author = postWriteForm.getAuthor();
        this.title = postWriteForm.getTitle();
        this.content = postWriteForm.getContent();
        this.url = postWriteForm.getUrl();
    }

    public Post(Member author, String title, String content, String url) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.url = url;
    }

    public void edit(PostEditForm postEditForm) {
        this.title = postEditForm.getTitle();
        this.content = postEditForm.getContent();
        this.url = postEditForm.getUrl();
    }

    // 로그에 찍히는 content 길이 최대 40글자
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(new PostToString(this), ToStringStyle.MULTI_LINE_STYLE);
    }
}

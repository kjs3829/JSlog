package com.jslog.blog.domain.post.form;

import com.jslog.blog.domain.member.Member;
import com.jslog.blog.domain.post.entity.Post;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter @Setter @ToString
public class PostWriteForm {
    private Member author;
    private String title;
    private String content;
    @NotEmpty
    private String url;
    private String tags;

}

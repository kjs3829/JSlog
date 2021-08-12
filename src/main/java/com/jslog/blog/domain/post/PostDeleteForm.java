package com.jslog.blog.domain.post;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class PostDeleteForm {
    public Long id;

    public PostDeleteForm(Long id) {
        this.id = id;
    }
}

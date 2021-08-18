package com.jslog.blog.domain.post.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity @Getter
public class PostWithTag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_no_tag_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public void setPost(Post post) {
        this.post = post;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}

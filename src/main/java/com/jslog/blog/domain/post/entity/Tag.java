package com.jslog.blog.domain.post.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor
public class Tag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private int id;

    @Column(name = "tag_name")
    private String name;

    @OneToMany(mappedBy = "tag")
    private List<PostWithTag> postWithTags = new ArrayList<>();

    public Tag(String name) {
        this.name = name;
    }

}

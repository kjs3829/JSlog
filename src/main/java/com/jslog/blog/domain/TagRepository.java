package com.jslog.blog.domain;

import com.jslog.blog.domain.post.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
@Transactional
public class TagRepository {
    private final EntityManager em;

    public int save(Tag tag) {
        em.persist(tag);
        return tag.getId();
    }
}

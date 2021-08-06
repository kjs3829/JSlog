package com.jslog.blog.domain.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostMySQLRepository implements PostRepository{
    private final EntityManager em;


    @Override
    public List<Post> findAll() {
        return em.createQuery("select p from Post p").getResultList();
    }

    @Override
    public Post findById(Long id) {
        Post findPost = em.find(Post.class, id);
        log.info("FindById Post = {}", findPost);
        return findPost;
    }

    @Override
    public Long save(Post post) {
        em.persist(post);
        log.info("Save Post = {}", post.toString());
        return post.getId();
    }

    @Override
    public void remove(Long id) {
        Post removePost = em.find(Post.class, id);
        log.info("Remove Post ={}", removePost);
        em.remove(removePost);
    }
}

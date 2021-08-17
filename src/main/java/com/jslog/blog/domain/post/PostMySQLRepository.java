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
    public Post findByUrl(String url) {
        List result = em.createQuery("select p from Post p where p.url = :url")
                .setParameter("url", url).getResultList();
        log.info("FindByUrl Post = {}", result);
        if (result.isEmpty()) {
            return null;
        }
        return (Post) result.get(0);
    }

    @Override
    public boolean hasUrl(String url) {
        Post result = findByUrl(url);
        return result != null;
    }

    @Override
    public Long save(PostWriteForm writeForm) {
        Post newPost = new Post(writeForm);
        em.persist(newPost);
        log.info("Save Post = {}", newPost.toString());
        return newPost.getId();
    }

    @Override
    public void delete(Long id) {
        Post removePost = em.find(Post.class, id);
        log.info("Remove Post ={}", removePost);
        em.remove(removePost);
    }

    @Override
    public Long update(Long id, PostEditForm postEditForm) {
        Post findPost = findById(id);
        findPost.edit(postEditForm);
        return id;
    }

    @Override
    public boolean hasId(Long id) {
        return em.find(Post.class, id) != null;
    }

    @Override
    public List<Post> getPage(Integer page) {
        if (page <= 0) {
            return null;
        }
        log.info("page = {}", page);
        List<Post> resultList = em.createQuery("select p from Post p order by p.id desc")
                .setFirstResult((page - 1) * 4 + 1)
                .setMaxResults(page * 4).getResultList();
        return resultList;
    }

    @Override
    public int getMaxPage() {
        Long singleResult = (Long) em.createQuery("select count(p) from Post p")
                .getSingleResult();
        return singleResult.intValue() / 4;
    }
}

package jslog.domain.post;

import jslog.domain.post.entity.Post;
import jslog.domain.post.form.PostEditForm;
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
        return em.createQuery("select p from Post p", Post.class).getResultList();
    }

    @Override
    public Post findById(Long id) {
        return em.find(Post.class, id);
    }

    @Override
    public Post findByUrl(String url) {
        List<Post> result = em.createQuery("select p from Post p where p.url = :url", Post.class)
                .setParameter("url", url).getResultList();
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public boolean hasUrl(String url) {
        Post result = findByUrl(url);
        return result != null;
    }

    @Override
    public Long save(Post post) {
        em.persist(post);
        log.info("Save Post = {}", post.toString());
        return post.getId();
    }

    @Override
    public void delete(Long id) {
        Post removePost = em.find(Post.class, id);
        log.info("id = {} Post is Removed.", id);
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
        return em.createQuery("select p from Post p order by p.id desc", Post.class)
                .setFirstResult((page - 1) * 4)
                .setMaxResults(page * 4).getResultList();
    }

    @Override
    public int getMaxPage() {
        Long singleResult = em.createQuery("select count(p) from Post p", Long.class)
                .getSingleResult();
        return singleResult.intValue() / 5 + 1;
    }

    @Override
    public Long getBeforePostId() {
        List<Post> resultList = em.createQuery("select p from Post p order by p.createdDate desc", Post.class)
                .setMaxResults(1).getResultList();
        if (resultList.size() == 0) return null;
        return resultList.get(0).getId();
    }
}

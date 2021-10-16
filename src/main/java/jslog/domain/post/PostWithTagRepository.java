package jslog.domain.post;

import jslog.domain.post.entity.Post;
import jslog.domain.post.entity.PostWithTag;
import jslog.domain.post.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class PostWithTagRepository {
    private final EntityManager em;

    public boolean isDeadTag(Tag tag) {
        Long tag1 = em.createQuery("select count(pt) from PostWithTag pt where pt.tag=:tag", Long.class)
                .setParameter("tag", tag)
                .getSingleResult();
        return tag1 == 0L;
    }

    public Long save(PostWithTag postWithTag) {
        em.persist(postWithTag);
        return postWithTag.getId();
    }

    public void delete(Long postId) {
        Post post = em.find(Post.class, postId);
        List<PostWithTag> postWithTags = em.createQuery("select pt from PostWithTag pt where pt.post=:post", PostWithTag.class)
                .setParameter("post", post).getResultList();
        for (PostWithTag postWithTag : postWithTags) {
            em.remove(postWithTag);
        }
    }
}

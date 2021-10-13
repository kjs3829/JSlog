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

    public Long save(Post post, Tag tag) {
        PostWithTag postWithTag = new PostWithTag();
        postWithTag.setPost(post);
        postWithTag.setTag(tag);
        em.persist(postWithTag);
        return postWithTag.getId();
    }

    public void delete(Long postId) {
        Post post = em.find(Post.class, postId);
        List postWithTags = em.createQuery("select pt from PostWithTag pt where pt.post=:post")
                .setParameter("post", post).getResultList();
        for (Object postWithTag : postWithTags) {
            em.remove(postWithTag);
        }
    }
}

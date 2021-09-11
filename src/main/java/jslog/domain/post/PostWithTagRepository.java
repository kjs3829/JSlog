package jslog.domain.post;

import jslog.domain.post.entity.Post;
import jslog.domain.post.entity.PostWithTag;
import jslog.domain.post.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

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
}

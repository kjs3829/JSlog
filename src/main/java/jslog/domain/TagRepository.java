package jslog.domain;

import jslog.domain.post.entity.Post;
import jslog.domain.post.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class TagRepository {
    private final EntityManager em;

    public Long save(Tag tag) {
        em.persist(tag);
        return tag.getId();
    }

    public Long delete(Tag tag) {
        em.remove(tag);
        return tag.getId();
    }

    public List<Tag> findAll() {
        return em.createQuery("SELECT t FROM Tag t", Tag.class).getResultList();
    }

    public List<Post> findPostByTag(String name) {
        return em.createQuery("SELECT p FROM PostWithTag pt JOIN pt.post p WHERE pt.tag.name = :name", Post.class)
                .setParameter("name", name).getResultList();
    }

    public Tag findByName(String name) {
        List<Tag> result =  em.createQuery("select t from Tag t where t.name =: name", Tag.class)
                .setParameter("name", name)
                .getResultList();
        if (result.isEmpty()) return null;
        return result.get(0);
    }
}

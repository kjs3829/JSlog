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

    public int save(Tag tag) {
        em.persist(tag);
        return tag.getId();
    }

    public List<Tag> findAll() {
        return em.createQuery("SELECT t FROM Tag t").getResultList();
    }

    public List<Post> findByTag(String name) {
        return em.createQuery("SELECT t.post FROM PostWithTag t WHERE t.tag.name = :name")
                .setParameter("name", name).getResultList();
    }
}

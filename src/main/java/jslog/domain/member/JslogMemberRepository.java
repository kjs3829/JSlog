package jslog.domain.member;

import jslog.domain.member.entity.JslogMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class JslogMemberRepository {
    private final EntityManager em;

    public Long add(JslogMember jslogMember) {
        em.persist(jslogMember);
        return jslogMember.getId();
    }

    public Long delete(JslogMember jslogMember) {
        em.remove(jslogMember);
        return jslogMember.getId();
    }

    public JslogMember findById(Long id) {
        return em.find(JslogMember.class, id);
    }

    public JslogMember findByEmail(String email) {
        List<JslogMember> result = em.createQuery("select jm from JslogMember jm where jm.email = :email", JslogMember.class)
                .setParameter("email", email)
                .getResultList();
        if (result.isEmpty()) return null;
        return result.get(0);
    }
}

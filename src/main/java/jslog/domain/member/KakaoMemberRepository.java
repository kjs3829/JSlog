package jslog.domain.member;

import jslog.domain.member.entity.KakaoMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
@Transactional
public class KakaoMemberRepository {
    private final EntityManager em;

    public Long add(KakaoMember kakaoMember) {
        em.persist(kakaoMember);
        return kakaoMember.getId();
    }

    public Long delete(KakaoMember kakaoMember) {
        em.remove(kakaoMember);
        return kakaoMember.getId();
    }

}

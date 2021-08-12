package com.jslog.blog.domain;

import com.jslog.blog.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberMySQLRepository implements MemberRepository{
    private final EntityManager em;

    @Override
    public List findAll() {
        return em.createQuery("select m from Member m").getResultList();
    }

    @Override
    public Member findById(Long id) {
        Member findMember = em.find(Member.class, id);
        log.info("FindById Member = {}", findMember);
        return findMember;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        Optional<Member> findMember = Optional.ofNullable(em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getSingleResult());
        log.info("FindByEmail Member = {}", findMember);
        return findMember;
    }

    @Override
    public Long add(Member member) {
        em.persist(member);
        log.info("Add Member = {}", member.toString());
        return member.getId();
    }

    @Override
    public Member delete(Long id) {
        Member member = findById(id);
        log.info("Delete Member = {}", member);
        em.remove(member);
        return member;
    }

}

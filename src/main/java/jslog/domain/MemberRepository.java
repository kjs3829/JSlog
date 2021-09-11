package jslog.domain;

import jslog.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    public List<Member> findAll();
    public Member findById(Long id);
    public Optional<Member> findByEmail(String email);
    public Long add(Member member);
    public Member delete(Long id);
}

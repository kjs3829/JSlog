package jslog.domain.member;

import jslog.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    public List<Member> findAll();
    public Member findById(Long id);
    public Optional<Member> findByEmail(String email);
}

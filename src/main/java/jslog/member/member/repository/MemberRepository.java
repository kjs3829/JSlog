package jslog.member.member.repository;

import jslog.member.auth.domain.ProviderName;
import jslog.member.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long id);

    @Query("select m from Member m where m.provider.provideId = :provideId and m.provider.providerName = :providerName")
    Optional<Member> findByProvideIdAndProviderName(@Param("provideId") String provideId, @Param("providerName") ProviderName providerName);

}

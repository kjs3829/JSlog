package jslog.tag.repository;

import jslog.tag.MemberTag;
import jslog.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {
    Optional<Tag> findTagByName(String name);

    @Query("select new jslog.tag.MemberTag(a.id, COUNT(t.name), t.name) from Tag t join t.postWithTags pt left join pt.post p join p.author a where a.id = :authorId group by t.name")
    List<MemberTag> getMemberTags(@Param("authorId") Long authorId);
}

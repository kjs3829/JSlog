package jslog.postWithTag.repository;

import jslog.postWithTag.domain.PostWithTag;
import jslog.post.ui.dto.MemberTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostWithTagRepository extends JpaRepository<PostWithTag, Long> {

    @Query("select new jslog.post.ui.dto.MemberTag(a.id, COUNT(pwt.id), t.name) from PostWithTag pwt join pwt.tag t join pwt.post p left join p.author a where a.id = :authorId group by t.name")
    List<MemberTag> getMemberTags(@Param("authorId") Long authorId);

}

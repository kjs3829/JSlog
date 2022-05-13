package jslog.likes.repository;

import jslog.likes.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes,Long> {

    @Query("select count(l) from Likes l where l.post.id = :postId")
    int countLikesByPostId(@Param("postId") Long postId);

    Optional<Likes> findByMemberIdAndPostId(Long memberId, Long postId);

    List<Likes> findByPostId(Long postId);
}

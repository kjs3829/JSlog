package jslog.postWithTag.repository;

import jslog.postWithTag.domain.PostWithTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostWithTagRepository extends JpaRepository<PostWithTag, Long> {
}

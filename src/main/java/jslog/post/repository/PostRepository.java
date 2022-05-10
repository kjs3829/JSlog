package jslog.post.repository;

import jslog.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {

    Optional<Post> findById(Long id);

    Optional<Post> findByAuthorIdAndUrl(Long authorId, String url);

    Slice<Post> findSliceByAuthorIdOrderByCreatedDateDesc(Long authorId, Pageable pageable);

    Page<Post> findByAuthorIdAndTitleContainingOrderByCreatedDateDesc(Long authorId, String q, Pageable pageable);

    Page<Post> findByAuthorIdAndPostWithTagsTagNameOrderByCreatedDateDesc(Long authorId, String tag, Pageable pageable);

    Page<Post> findByAuthorIdOrderByCreatedDateDesc(Long authorId, Pageable pageable);

    Page<Post> findByTitleContainingOrderByCreatedDateDesc(String q, Pageable pageable);


}

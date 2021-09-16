package jslog.domain.post;

import jslog.domain.TagRepository;
import jslog.domain.post.entity.Post;
import jslog.domain.post.entity.Tag;
import jslog.domain.post.form.PostWriteForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostWithTagRepository postWithTagRepository;

    @Transactional
    public Long write(PostWriteForm form) {
        Long beforePageId = postRepository.getBeforePostId();
        Post post = new Post.Builder().setAuthor(form.getAuthor())
                .setContent(form.getContent())
                .setTitle(form.getTitle())
                .setUrl(form.getUrl())
                .setPreview(form.getPreview())
                .setBeforePostId(beforePageId)
                .build();
        postRepository.save(post);
        setBeforePostNext(post.getId(), beforePageId);
        String tags = form.getTags();
        String[] tagList = tags.split(",");
        for (String tagName : tagList) {
            Tag tag = new Tag(tagName.trim());
            tagRepository.save(tag);
            postWithTagRepository.save(post, tag);
        }
        return post.getId();
    }

    @Transactional
    public void delete(Long deletePostId) {
        Post findPost = postRepository.findById(deletePostId);

        // 이전포스트, 다음포스트 재배치
        if (findPost.getBeforePostId() != null) {
            Post bp = postRepository.findById(findPost.getBeforePostId());
            if (findPost.getNextPostId() != null) bp.setNextPostId(findPost.getNextPostId());
            else bp.setNextPostId(null);
        }
        if (findPost.getNextPostId() != null) {
            Post np = postRepository.findById(findPost.getNextPostId());
            if (findPost.getBeforePostId() != null) np.setBeforePostId(findPost.getBeforePostId());
            else np.setBeforePostId(null);
        }

        postRepository.delete(deletePostId);
    }

    @Transactional
    private void setBeforePostNext(Long newPostId, Long beforePostId) {
        Post findPost = postRepository.findById(beforePostId);
        findPost.setNextPostId(newPostId);
    }

}

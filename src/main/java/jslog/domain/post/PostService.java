package jslog.domain.post;

import jslog.domain.TagRepository;
import jslog.domain.post.entity.Post;
import jslog.domain.post.entity.PostWithTag;
import jslog.domain.post.entity.Tag;
import jslog.domain.post.form.PostReadForm;
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

    public PostReadForm postToReadForm(Post post) {
        PostReadForm readForm = post.toReadForm();
        if (post.getBeforePostId() != null) {
            Post bp = postRepository.findById(post.getBeforePostId());
            readForm.setBeforePostTitle(bp.getTitle());
            readForm.setBeforePostUrl(bp.getUrl());
        }
        if (post.getNextPostId() != null) {
            Post np = postRepository.findById(post.getNextPostId());
            readForm.setNextPostTitle(np.getTitle());
            readForm.setNextPostUrl(np.getUrl());
        }
        return readForm;
    }

    @Transactional
    public Long write(PostWriteForm form) {
        Long beforePageId = postRepository.getBeforePostId();
        Post post = Post.builder().author(form.getAuthor())
                .content(form.getContent())
                .title(form.getTitle())
                .url(form.getUrl())
                .preview(form.getPreview())
                .beforePostId(beforePageId)
                .build();
        postRepository.save(post);
        if (beforePageId != null) setBeforePostNext(post.getId(), beforePageId);
        String tagString = form.getTags().trim();
        if (!tagString.equals("")) {
            String[] tags = tagString.split(",");
            for (String tag : tags) {
                String tagName = tag.trim();
                Tag findTag = tagRepository.findByName(tagName);
                if (findTag == null) {
                    Tag newTag = new Tag(tagName);
                    tagRepository.save(newTag);
                    postWithTagRepository.save(new PostWithTag(post, newTag));
                } else postWithTagRepository.save(new PostWithTag(post, findTag));
            }
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
        postWithTagRepository.delete(deletePostId);
        postRepository.delete(deletePostId);
        for (Tag tag : findPost.getTags()) {
            if (postWithTagRepository.isDeadTag(tag)) tagRepository.delete(tag);
        }
    }

    @Transactional
    private void setBeforePostNext(Long newPostId, Long beforePostId) {
        Post findPost = postRepository.findById(beforePostId);
        findPost.setNextPostId(newPostId);
    }

}

package jslog.domain.post;

import jslog.domain.TagRepository;
import jslog.domain.post.entity.Post;
import jslog.domain.post.entity.Tag;
import jslog.domain.post.form.PostWriteForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostWithTagRepository postWithTagRepository;

    public Long write(PostWriteForm form) {
        Post post = new Post.Builder().setAuthor(form.getAuthor())
                .setContent(form.getContent())
                .setTitle(form.getTitle())
                .setUrl(form.getUrl())
                .setPreview(form.getPreview())
                .build();
        postRepository.save(post);
        String tags = form.getTags();
        String[] tagList = tags.split(",");
        for (String tagName : tagList) {
            Tag tag = new Tag(tagName);
            tagRepository.save(tag);
            postWithTagRepository.save(post, tag);
        }
        return post.getId();
    }
}

package com.jslog.blog.domain.post;

import com.jslog.blog.domain.TagRepository;
import com.jslog.blog.domain.post.entity.Post;
import com.jslog.blog.domain.post.entity.Tag;
import com.jslog.blog.domain.post.form.PostWriteForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

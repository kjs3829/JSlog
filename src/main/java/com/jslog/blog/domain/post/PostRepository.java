package com.jslog.blog.domain.post;

import com.jslog.blog.domain.post.entity.Post;
import com.jslog.blog.domain.post.form.PostEditForm;

import java.util.List;

public interface PostRepository {
    public List<Post> findAll();
    public Post findById(Long id);
    public Long save(Post post);
    public void delete(Long id);
    public Long update(Long id, PostEditForm postEditForm);
    public Post findByUrl(String url);
    public boolean hasUrl(String url);
    public boolean hasId(Long id);
    public List<Post> getPage(Integer page);
    public int getMaxPage();
}

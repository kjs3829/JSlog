package com.jslog.blog.domain.post;

import java.util.List;

public interface PostRepository {
    public List<Post> findAll();
    public Post findById(Long id);
    public Long save(Post post);
    public void remove(Long id);
}

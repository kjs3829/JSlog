package com.jslog.blog.domain.post;

import java.util.List;

public interface PostRepository {
    public List<Post> findAll();
    public Post findById(Long id);
    public Long save(PostWriteForm postWriteForm);
    public void delete(Long id);
    public Long update(Long id, PostEditForm postEditForm);
    public Post findByUrl(String url);
    public boolean hasUrl(String url);
    public boolean hasId(Long id);
}

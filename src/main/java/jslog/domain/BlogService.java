package jslog.domain;

import jslog.domain.post.PostRepository;
import jslog.domain.post.entity.Post;
import jslog.domain.post.form.PostSelectForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
    public final PostRepository postRepository;

    public List<PostSelectForm> getPage(int page) {
        List<Post> findPage = postRepository.getPage(page);
        List<PostSelectForm> result = new ArrayList<PostSelectForm>();
        for (Post post : findPage) {
            result.add(post.toSelectForm());
        }
        return result;
    }
}

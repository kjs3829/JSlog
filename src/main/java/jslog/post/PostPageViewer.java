package jslog.post;

import jslog.commons.PageViewer;
import jslog.post.domain.Post;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PostPageViewer extends PageViewer {
    SearchCondition searchCondition;
    String tag;
    String q;

    public PostPageViewer(SearchCondition searchCondition, String q, String tag, Page<Post> page, int pageSize) {
        super(page, pageSize);
        this.q = q;
        this.tag = tag;
        this.searchCondition = searchCondition;
    }

    public boolean isNoConditionSearch() {
        return searchCondition.equals(SearchCondition.none);
    }

    public boolean isTagConditionSearch() {
        return searchCondition.equals(SearchCondition.tag);
    }

}

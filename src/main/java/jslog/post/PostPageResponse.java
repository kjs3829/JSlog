package jslog.post;

import jslog.commons.PageResponse;
import jslog.post.domain.Post;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter @EqualsAndHashCode(callSuper = true)
public class PostPageResponse extends PageResponse {
    SearchCondition searchCondition;
    String tag;
    String q;

    public PostPageResponse(SearchCondition searchCondition, String q, String tag, Page<Post> page) {
        super(page);
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

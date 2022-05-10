package jslog.post.ui.dto;

import jslog.post.domain.Post;
import jslog.tag.domain.Tag;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter @Slf4j
public class PostSelectForm {
    private String title;
    private Long AuthorId;
    private String url;
    private String createdDate;
    private List<Tag> tags;
    private String preview;

    /*  TODO
    private int comments;
    private String series;
     */

    public PostSelectForm(Post post) {
        this.AuthorId = post.getAuthor().getId();
        this.title = post.getTitle();
        this.url = post.getUrl();
        this.createdDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"));
        this.tags = post.getTags();
        this.preview = post.getPreview();
    }
}

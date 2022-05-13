package jslog.post.ui.dto;

import jslog.comment.ui.dto.CommentDto;
import jslog.comment.ui.dto.CommentResponse;
import jslog.member.member.domain.Member;
import jslog.post.domain.Post;
import jslog.tag.domain.Tag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.List;

@Getter @Setter @ToString
public class PostReadForm {
    private Long id;
    private Member author;
    private String title;
    private String content;
    private String url;
    private List<Tag> tags;
    private String createdDate;
    private Long beforePostAuthorId;
    private Long nextPostAuthorId;
    private String beforePostUrl;
    private String nextPostUrl;
    private String beforePostTitle;
    private String nextPostTitle;

    private LikesResponse likesResponse;
    private CommentResponse commentResponse;

    public PostReadForm(Post post, List<CommentDto> commentDtoList, LikesResponse likesResponse) {
        this.id = post.getId();
        this.author = post.getAuthor();
        this.title = post.getTitle();
        this.content = contentsParser(post.getContent());
        this.url = post.getStringUrl();
        this.tags = post.getTags();
        this.createdDate = post.getCreatedDateYYYYMMDD();
        commentResponse = CommentResponse.create(commentDtoList);
        this.likesResponse = likesResponse;
    }

    /**
     * markdown으로 작성된 게시글의 내용을 html 형식으로 바꾼다.
     */
    public String contentsParser(String contents) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(contents);
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        return renderer.render(document);
    }
}

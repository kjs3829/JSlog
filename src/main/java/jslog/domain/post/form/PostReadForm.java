package jslog.domain.post.form;

import jslog.domain.member.Member;
import jslog.domain.post.entity.Post;
import jslog.domain.post.entity.Tag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.time.format.DateTimeFormatter;
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

    public PostReadForm(Post post) {
        this.id = post.getId();
        this.author = post.getAuthor();
        this.title = post.getTitle();
        this.content = contentsParser(post.getContent());
        this.url = post.getUrl();
        this.tags = post.getTags();
        this.createdDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"));
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

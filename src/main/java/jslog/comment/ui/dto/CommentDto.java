package jslog.comment.ui.dto;

import jslog.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
@AllArgsConstructor
public class CommentDto {
    Long id;
    Long postId;
    Long authorId;
    String authorNickName;
    String content;
    String CreatedDate;

    public static CommentDto create(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getPost().getId(),
                comment.getAuthor().getId(),
                comment.getAuthor().getNickname(),
                comment.getContent(),
                comment.getCreatedDateYYYYMMDD());
    }
}

package jslog.comment.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class CommentEditRequest {
    Long commentId;
    String content;
}

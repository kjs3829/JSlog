package jslog.comment.ui.dto;

import lombok.*;

import java.util.List;

@Data
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponse {
    List<CommentDto> commentDtoList;
    int count;

    public static CommentResponse create(List<CommentDto> commentDtoList) {
        return new CommentResponse(commentDtoList, commentDtoList.size());
    }
}

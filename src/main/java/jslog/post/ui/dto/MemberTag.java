package jslog.post.ui.dto;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode
public class MemberTag {
    Long authorId;
    Long tagCount;
    String tagName;
}

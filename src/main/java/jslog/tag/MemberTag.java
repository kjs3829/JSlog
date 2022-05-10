package jslog.tag;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class MemberTag {
    Long authorId;
    Long tagCount;
    String tagName;
}

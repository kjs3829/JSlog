package jslog.tag;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode
public class MemberTag {
    Long authorId;
    Long tagCount;
    String tagName;
}

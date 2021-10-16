package jslog.domain.member.entity;

import jslog.domain.member.MemberRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity @Getter @NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class JslogMember extends Member{
    private String password;

    @Builder
    public JslogMember(String email, String nickname, MemberRole memberRole, String password) {
        super(email, nickname, memberRole);
        this.password = password;
    }
}

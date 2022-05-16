package jslog.member.member.domain;

import jslog.member.auth.domain.Provider;
import jslog.member.member.ui.dto.ProfileUpdateForm;
import jslog.commons.domain.BaseEntity;
import jslog.post.domain.Post;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Embedded
    private Provider provider;

    private String nickname;
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    public static Member create(Provider provider, String nickname, MemberRole memberRole) {
        return new Member(null, provider, nickname, memberRole);
    }

    public ProfileUpdateForm getProfileUpdateForm() {
        return new ProfileUpdateForm(this.getNickname());
    }

    public void updateProfile(ProfileUpdateForm profileUpdateForm) {
        this.nickname = profileUpdateForm.getNickname();
    }
}

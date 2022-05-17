package jslog.member.member.ui.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProfileUpdateRequest {
    String nickname;

    @Builder
    public ProfileUpdateRequest(String nickname) {
        this.nickname = nickname;
    }
}

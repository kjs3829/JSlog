package jslog.member.member.ui.dto;


import lombok.Getter;
import lombok.Setter;


public class ProfileUpdateForm {

    @Getter @Setter
    String nickname;

    public ProfileUpdateForm(String nickname) {
        this.nickname = nickname;
    }
}

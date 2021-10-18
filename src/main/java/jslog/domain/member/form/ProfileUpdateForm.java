package jslog.domain.member.form;


import lombok.Getter;
import lombok.Setter;


public class ProfileUpdateForm {

    @Getter
    String email;
    @Getter @Setter
    String nickname;

    public ProfileUpdateForm(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}

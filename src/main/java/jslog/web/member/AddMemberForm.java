package jslog.web.member;

import lombok.Data;

@Data
public class AddMemberForm {
    private String email;
    private String password;
    private String nickname;

    public AddMemberForm() {
    }

    public AddMemberForm(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}

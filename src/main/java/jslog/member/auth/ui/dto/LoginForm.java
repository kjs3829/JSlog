package jslog.member.auth.ui.dto;

import jslog.member.auth.domain.ProviderName;
import lombok.Data;

@Data
public class LoginForm {
    private String email;
    private String password;
    private ProviderName providerName;
}

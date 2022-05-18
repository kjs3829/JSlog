package jslog.member.auth.controller;

import jslog.member.auth.application.LoginService;
import jslog.member.auth.infrastructure.kakao.KakaoOauthInfo;
import jslog.member.auth.ui.LoginController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean LoginService loginService;
    @MockBean KakaoOauthInfo kakaoOauthInfo;

    LoginController loginController;

    @BeforeEach
    void setUp() {
        loginController = new LoginController(loginService, kakaoOauthInfo);
    }

    @Test
    @DisplayName("로그인 페이지를 요청한다.")
    void loginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-in"))
                .andDo(print());
    }

    @Test
    @DisplayName("카카오 계정으로 로그인 한다.")
    void kakaoLogin() throws Exception {
        mockMvc.perform(get("/auth/kakao/login")
                .param("code","code"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());

        verify(loginService).createSession(any(), any(), any());
    }

}

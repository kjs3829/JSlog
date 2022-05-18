package jslog.commons.interceptor;

import jslog.member.auth.ui.LoginMember;
import jslog.member.member.domain.MemberRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginCheckInterceptorTest {
    @Mock HttpServletRequest request;
    @Mock HttpServletResponse response;
    @Mock HttpSession session;

    LoginCheckInterceptor loginCheckInterceptor = new LoginCheckInterceptor();
    LoginMember loginMember;

    @BeforeEach
    void setUp() {
        loginMember = new LoginMember(1L,"테스트 유저", MemberRole.MEMBER);
    }

    @Test
    @DisplayName("올바른 세션 접근")
    void login() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(any())).thenReturn(loginMember);

        boolean loginCheck = loginCheckInterceptor.preHandle(request, response, new Object());

        assertThat(loginCheck).isTrue();
    }

    @Test
    @DisplayName("세션이 없는 사용자의 접근")
    void no_session_access() throws Exception {
        when(request.getRequestURI()).thenReturn("re");
        when(request.getSession(false)).thenReturn(null);

        boolean loginCheck = loginCheckInterceptor.preHandle(request, response, new Object());

        verify(response).sendRedirect("/login?redirectURL="+"re");
        assertThat(loginCheck).isFalse();
    }

    @Test
    @DisplayName("알수없는 오류에 의해 세션에 사용자 정보가 없는 경우")
    void no_session_attribute_access() throws Exception {
        when(request.getRequestURI()).thenReturn("re");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(null);

        boolean loginCheck = loginCheckInterceptor.preHandle(request, response, new Object());

        verify(response).sendRedirect("/login?redirectURL="+"re");
        assertThat(loginCheck).isFalse();
    }

}

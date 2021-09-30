package jslog.web.interceptor;

import jslog.domain.member.Member;
import jslog.domain.member.MemberRole;
import jslog.web.SessionConst;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (!member.getMemberRole().equals(MemberRole.ADMIN)) {
            response.sendRedirect("/login?requestURL=" + requestURI);
            return false;
        }

        return true;
    }
}

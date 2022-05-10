package jslog.commons.interceptor;

import jslog.member.member.domain.Member;
import jslog.member.member.domain.MemberRole;
import jslog.commons.SessionConst;
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

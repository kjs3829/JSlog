package jslog.member.member.ui;

import jslog.commons.SessionConst;
import jslog.member.auth.ui.LoginMember;
import jslog.member.member.application.MemberService;
import jslog.member.member.domain.Member;
import jslog.member.member.domain.MemberRole;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
class MemberControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean MemberService memberService;
    MemberController memberController;

    Member member;
    LoginMember loginMember;

    @BeforeEach
    void setUp() {
        memberController = new MemberController(memberService);

        member = Member.builder().id(1L).nickname("테스트 유저").memberRole(MemberRole.MEMBER).build();

        loginMember = LoginMember.createMember(member);
    }

    @Test
    @DisplayName("회원 정보 수정 페이지를 요청한다.")
    void settingMemberForm() throws Exception {
        mockMvc.perform(get("/members/setting")
                .sessionAttr(SessionConst.LOGIN_MEMBER,loginMember))
                .andExpect(status().isOk())
                .andExpect(view().name("member-setting"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 정보를 수정한다.")
    void updateMemberProfile() throws Exception {
        mockMvc.perform(post("/members/setting")
                .sessionAttr(SessionConst.LOGIN_MEMBER,loginMember))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/setting"))
                .andDo(print());

        verify(memberService).updateMember(any(),any(),any());
    }

}
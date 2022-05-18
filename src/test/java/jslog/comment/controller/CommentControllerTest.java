package jslog.comment.controller;

import jslog.comment.application.CommentService;
import jslog.comment.domain.Comment;
import jslog.comment.repository.CommentRepository;
import jslog.comment.ui.CommentController;
import jslog.commons.SessionConst;
import jslog.member.auth.ui.LoginMember;
import jslog.member.member.domain.Member;
import jslog.member.member.domain.MemberRole;
import jslog.post.domain.Post;
import jslog.post.domain.url.CustomUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {
    @Autowired MockMvc mockMvc;

    @MockBean CommentService commentService;
    @MockBean CommentRepository commentRepository;

    CommentController commentController;
    Member member;
    LoginMember loginMember;
    Post post;

    @BeforeEach
    void setUp() {
        member = Member.builder().id(1L).nickname("테스트 유저").memberRole(MemberRole.MEMBER).build();

        loginMember = LoginMember.createMember(member);

        post = Post.builder().id(1L).author(member).title("제목").customUrl(CustomUrl.create("테스트 URL")).build();

        commentController = new CommentController(commentRepository,commentService);
    }

    @Test
    @DisplayName("댓글 작성 - 성공")
    void write() throws Exception {

        mockMvc.perform(post("/comments")
                .param("redirectUrl",post.getStringUrl())
                .param("redirectAuthorId",post.getAuthor().getId().toString())
                .param("postId",post.getId().toString())
                .param("comment","댓글내용")
                .sessionAttr(SessionConst.LOGIN_MEMBER,loginMember))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/"+post.getAuthor().getId()+"/"+ URLEncoder.encode(post.getStringUrl(), StandardCharsets.UTF_8)))
                .andDo(print());

        verify(commentService).write(anyString(),any(),any());
    }

    @Test
    @DisplayName("댓글 작성 - 실패(비로그인 사용자가 댓글을 작성)")
    void write_fail_non_login() throws Exception {

        mockMvc.perform(post("/comments")
                .param("redirectUrl",post.getStringUrl())
                .param("redirectAuthorId",post.getAuthor().getId().toString())
                .param("postId",post.getId().toString())
                .param("comment","댓글내용"))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 수정 페이지 요청")
    void editPage() throws Exception {
        Comment comment = new Comment();
        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

        mockMvc.perform(get("/comments/edit")
                .param("redirectUrl",post.getStringUrl())
                .param("redirectAuthorId",post.getAuthor().getId().toString())
                .param("commentId","1"))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 수정 성공")
    void edit() throws Exception {

        mockMvc.perform(post("/comments/edit")
                .param("redirectUrl",post.getStringUrl())
                .param("redirectAuthorId",post.getAuthor().getId().toString())
                .sessionAttr(SessionConst.LOGIN_MEMBER,loginMember))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/"+post.getAuthor().getId()+"/"+URLEncoder.encode(post.getStringUrl(),StandardCharsets.UTF_8)))
                .andDo(print());

        verify(commentService).edit(any(),any());
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void delete() throws Exception {
        mockMvc.perform(post("/comments/delete")
                .param("redirectUrl",post.getStringUrl())
                .param("redirectAuthorId",post.getAuthor().getId().toString()).param("commentId", "1")
                .sessionAttr(SessionConst.LOGIN_MEMBER,loginMember))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/"+post.getAuthor().getId()+"/"+URLEncoder.encode(post.getStringUrl(),StandardCharsets.UTF_8)))
                .andDo(print());

        verify(commentService).delete(any(),any());
    }
}

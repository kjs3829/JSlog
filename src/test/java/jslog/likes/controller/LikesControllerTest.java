package jslog.likes.controller;

import jslog.comment.ui.dto.CommentResponse;
import jslog.likes.application.LikesService;
import jslog.likes.domain.Likes;
import jslog.likes.ui.LikesController;
import jslog.member.auth.ui.LoginMember;
import jslog.member.member.domain.Member;
import jslog.post.domain.Post;
import jslog.post.domain.url.CustomUrl;
import jslog.post.ui.dto.LikesResponse;
import jslog.post.ui.dto.PostReadResponse;
import jslog.post.ui.dto.PostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LikesController.class)
public class LikesControllerTest {
    @Autowired MockMvc mockMvc;

    @MockBean LikesService likesService;

    @Test
    @DisplayName("게시글에 좋아요를 누른다.")
    void like() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .build();
        LoginMember loginMember = LoginMember.createMember(member);

        Post post = Post.builder().customUrl(CustomUrl.create("testUrl")).build();

        Likes likes = Likes.create(member, post);

        when(likesService.like(any(),any())).thenReturn(1L);

        mockMvc.perform(get("/likes/like")
                .sessionAttr("loginMember",loginMember)
                .param("postId","1")
                .param("redirectAuthorId","1")
                .param("redirectUrl","testUrl"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 좋아요를 취소한다.")
    void unlike() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .build();
        LoginMember loginMember = LoginMember.createMember(member);

        doNothing().when(likesService).unlike(any(),any());

        mockMvc.perform(get("/likes/unlike")
                .sessionAttr("loginMember",loginMember)
                .param("postId","1")
                .param("redirectAuthorId","1")
                .param("redirectUrl","testUrl"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }
}

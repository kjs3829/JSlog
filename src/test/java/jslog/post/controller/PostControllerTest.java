package jslog.post.controller;

import jslog.comment.ui.dto.CommentResponse;
import jslog.member.auth.ui.LoginMember;
import jslog.member.member.domain.Member;
import jslog.post.PostPageResponse;
import jslog.post.SearchCondition;
import jslog.post.application.PostService;
import jslog.post.domain.Post;
import jslog.post.domain.url.CustomUrl;
import jslog.post.repository.PostRepository;
import jslog.post.ui.PostController;
import jslog.post.ui.dto.*;
import jslog.tag.MemberTag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired MockMvc mockMvc;

    @MockBean PostService postService;
    @MockBean PostRepository postRepository;

    @Test
    @DisplayName("포스트를 조회한다.")
    void getPost() throws Exception {

        Member member = Member.builder()
                .id(1L)
                .build();
        PostResponse postResponse = PostResponse.builder()
                .author(member)
                .url("테스트")
                .build();

        PostReadResponse postReadResponse = PostReadResponse.create(postResponse, null, null,
                CommentResponse.create(new ArrayList<>()), LikesResponse.create(0, false, false));

        when(postService.getPostReadResponse(any(),anyString(),any())).thenReturn(postReadResponse);

        mockMvc.perform(get("/posts/1/테스트"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("블로그홈을 조회한다")
    void getMemberBlogHome() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .build();
        MemberTag memberTag = new MemberTag(member.getId(), 1L, "테스트태그");

        PostPageResponse postPageResponse = new PostPageResponse(SearchCondition.none, null, null, Page.empty());

        when(postService.getMemberTags(any())).thenReturn(new ArrayList<>(Arrays.asList(memberTag)));
        when(postService.getPostPageResponse(1L,null,null,null)).thenReturn(postPageResponse);

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성 페이지를 요청한다.")
    void getWritePage() throws Exception {
        mockMvc.perform(get("/posts/write"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성에 실패한다. - 중복 URL")
    void write_fail_caused_by_duplicated_url() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .build();
        LoginMember loginMember = LoginMember.createMember(member);

        when(postService.isDuplicatedUrl(any(),anyString())).thenReturn(true);

        mockMvc.perform(post("/posts")
                .param("url","url")
                .sessionAttr("loginMember",loginMember))
                .andExpect(status().isOk())
                .andExpect(view().name("blog/write"))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성에 성공한다.")
    void write() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .build();
        LoginMember loginMember = LoginMember.createMember(member);

        when(postService.isDuplicatedUrl(any(),anyString())).thenReturn(false);

        when(postService.writePost(any(), any())).thenReturn(Post.builder().build());

        mockMvc.perform(post("/posts")
                .param("url","url")
                .sessionAttr("loginMember",loginMember))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정 페이지를 요청한다.")
    void getEditPage() throws Exception {
        Post post = Post.builder()
                .customUrl(CustomUrl.create("testUrl"))
                .build();

        when(postRepository.findById(any())).thenReturn(Optional.ofNullable(post));

        mockMvc.perform(get("/posts/edit")
                .param("postId","1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정에 실패한다. - 중복 URL")
    void edit_fail_caused_by_duplicated_url() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .build();
        LoginMember loginMember = LoginMember.createMember(member);

        when(postService.updatePost(any(),any())).thenReturn(false);


        mockMvc.perform(post("/posts/edit")
                .param("url","url")
                .sessionAttr("loginMember",loginMember))
                .andExpect(view().name("blog/edit"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정에 성공한다.")
    void edit() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .build();
        LoginMember loginMember = LoginMember.createMember(member);

        when(postService.updatePost(any(),any())).thenReturn(true);


        mockMvc.perform(post("/posts/edit")
                .param("url","url")
                .sessionAttr("loginMember",loginMember))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제에 성공한다.")
    void delete() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .build();
        LoginMember loginMember = LoginMember.createMember(member);

        when(postService.deletePost(any(),any())).thenReturn(1L);

        mockMvc.perform(post("/posts/delete")
                .param("postId","1")
                .sessionAttr("loginMember",loginMember))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

}

package jslog.likes.application;

import jslog.likes.domain.Likes;
import jslog.likes.repository.LikesRepository;
import jslog.member.auth.ui.LoginMember;
import jslog.member.member.domain.Member;
import jslog.member.member.domain.MemberRole;
import jslog.member.member.repository.MemberRepository;
import jslog.post.domain.Post;
import jslog.post.repository.PostRepository;
import jslog.post.ui.dto.LikesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikesServiceTest {

    @Mock MemberRepository memberRepository;
    @Mock PostRepository postRepository;
    @Mock LikesRepository likesRepository;
    LikesService likesService;

    Member member;
    LoginMember loginMember;
    Post post;
    Likes likes;

    @BeforeEach
    void setUp() {
        likesService = new LikesService(memberRepository,postRepository,likesRepository);

        member = Member.builder()
                .id(1L)
                .nickname("테스트유저")
                .memberRole(MemberRole.MEMBER)
                .build();

        loginMember = LoginMember.createMember(member);

        post = Post.builder()
                .id(1L)
                .author(member)
                .title("첫번째 게시글 제목")
                .content("첫번째 게시글 내용!!")
                .build();

        likes = Likes.create(member, post);
    }

    @Test
    @DisplayName("비로그인 사용자가 게시글의 좋아요를 조회한다.")
    void createLikesResponse_by_non_login_user() {
        when(likesRepository.countLikesByPostId(any())).thenReturn(0);

        LikesResponse likesResponse = likesService.createLikesResponse(null, 1L);

        assertThat(likesResponse).isEqualTo(LikesResponse.create(0, false, false));
    }

    @Test
    @DisplayName("게시글에 좋아요를 누르지 않은 로그인 사용자가 게시글의 좋아요를 조회한다.")
    void createLikesResponse_by_non_like_login_user() {
        when(likesRepository.countLikesByPostId(any())).thenReturn(0);
        when(likesRepository.findByMemberIdAndPostId(any(),any())).thenReturn(Optional.empty());

        LikesResponse likesResponse = likesService.createLikesResponse(loginMember, 1L);

        assertThat(likesResponse).isEqualTo(LikesResponse.create(0, true, false));
    }

    @Test
    @DisplayName("게시글에 좋아요를 누른 로그인 사용자가 게시글의 좋아요를 조회한다.")
    void createLikesResponse_by_liked_login_user() {
        when(likesRepository.countLikesByPostId(any())).thenReturn(0);
        when(likesRepository.findByMemberIdAndPostId(any(),any())).thenReturn(Optional.of(likes));

        LikesResponse likesResponse = likesService.createLikesResponse(loginMember, 1L);

        assertThat(likesResponse).isEqualTo(LikesResponse.create(0, true, true));
    }

    @Test
    @DisplayName("게시글에 좋아요를 누른다.")
    void like() {
        when(likesRepository.save(any())).thenReturn(likes);
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(postRepository.findById(any())).thenReturn(Optional.of(post));

        Long likesId = likesService.like(loginMember.getId(), post.getId());

        assertThat(likesId).isEqualTo(likes.getId());
    }

    @Test
    @DisplayName("게시글의 좋아요를 취소한다.")
    void unlike() {
        when(likesRepository.findByMemberIdAndPostId(any(),any())).thenReturn(Optional.of(likes));

        likesService.unlike(loginMember.getId(), post.getId());

        verify(likesRepository).delete(likes);
    }

    @Test
    @DisplayName("게시글에 눌린 모든 좋아요를 삭제한다.")
    void deleteLikesByPostId() {
        when(likesRepository.findByPostId(any())).thenReturn(new ArrayList<>(Arrays.asList(likes)));

        likesService.deleteLikesByPostId(post.getId());

        verify(likesRepository).delete(likes);
    }

}
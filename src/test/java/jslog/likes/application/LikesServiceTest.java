package jslog.likes.application;

import jslog.likes.domain.Likes;
import jslog.likes.repository.LikesRepository;
import jslog.member.auth.domain.Provider;
import jslog.member.auth.domain.ProviderName;
import jslog.member.auth.ui.LoginMember;
import jslog.member.member.domain.Member;
import jslog.member.member.domain.MemberRole;
import jslog.member.member.repository.MemberRepository;
import jslog.post.domain.Post;
import jslog.post.domain.url.CustomUrl;
import jslog.post.repository.PostRepository;
import jslog.post.ui.dto.LikesResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LikesServiceTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private LikesService likesService;
    @Autowired private LikesRepository likesRepository;

    @Test
    @DisplayName("비로그인 사용자의 likesResponse 생성 성공")
    void non_login_user_createLikesResponse() {
        //given
        Member member = Member.create(Provider.create("KAKAOID", ProviderName.KAKAO),"Tester1", MemberRole.MEMBER);
        memberRepository.save(member);

        Post post = Post.builder().customUrl(CustomUrl.create("1")).author(member).build();
        postRepository.save(post);

        //when
        LikesResponse likesResponse = likesService.createLikesResponse(null, post.getId());

        //then
        assertThat(likesResponse.getLikes()).isEqualTo(likesRepository.countLikesByPostId(post.getId()));
        assertThat(likesResponse.isLogin()).isEqualTo(false);
        assertThat(likesResponse.isLiked()).isEqualTo(false);
    }

    @Test
    @DisplayName("로그인 사용자이면서 좋아요를 누르지 않은 likesResponse 생성 성공")
    void login_user_non_like_createLikesResponse() {
        //given
        Member member = Member.create(Provider.create("KAKAOID", ProviderName.KAKAO),"Tester1", MemberRole.MEMBER);
        memberRepository.save(member);
        LoginMember loginMember = LoginMember.createMember(member);

        Post post = Post.builder().customUrl(CustomUrl.create("1")).author(member).build();
        postRepository.save(post);

        //when
        LikesResponse likesResponse = likesService.createLikesResponse(loginMember, post.getId());

        //then
        assertThat(likesResponse.getLikes()).isEqualTo(likesRepository.countLikesByPostId(post.getId()));
        assertThat(likesResponse.isLogin()).isEqualTo(true);
        assertThat(likesResponse.isLiked()).isEqualTo(false);
    }

    @Test
    @DisplayName("로그인 사용자이면서 좋아요를 누른 likesResponse 생성 성공")
    void login_user_like_createLikesResponse() {
        //given
        Member member = Member.create(Provider.create("KAKAOID", ProviderName.KAKAO),"Tester1", MemberRole.MEMBER);
        memberRepository.save(member);
        LoginMember loginMember = LoginMember.createMember(member);

        Post post = Post.builder().customUrl(CustomUrl.create("1")).author(member).build();
        postRepository.save(post);

        likesRepository.save(Likes.create(member, post));

        //when
        LikesResponse likesResponse = likesService.createLikesResponse(loginMember, post.getId());

        //then
        assertThat(likesResponse.getLikes()).isEqualTo(likesRepository.countLikesByPostId(post.getId()));
        assertThat(likesResponse.isLogin()).isEqualTo(true);
        assertThat(likesResponse.isLiked()).isEqualTo(true);
    }

    @Test
    @DisplayName("좋아요 생성 성공")
    void like() {
        //given
        Member member = Member.create(Provider.create("KAKAOID", ProviderName.KAKAO),"Tester1", MemberRole.MEMBER);
        memberRepository.save(member);
        LoginMember loginMember = LoginMember.createMember(member);

        Post post = Post.builder().customUrl(CustomUrl.create("1")).author(member).build();
        postRepository.save(post);

        //when
        Likes likes = likesService.like(loginMember.getId(), post.getId());

        //then
        assertThat(likes.getMember()).isEqualTo(member);
        assertThat(likes.getPost()).isEqualTo(post);
    }

    @Test
    @DisplayName("좋아요 삭제 성공")
    void unlike() {
        //given
        Member member = Member.create(Provider.create("KAKAOID", ProviderName.KAKAO),"Tester1", MemberRole.MEMBER);
        memberRepository.save(member);
        LoginMember loginMember = LoginMember.createMember(member);

        Post post = Post.builder().customUrl(CustomUrl.create("1")).author(member).build();
        postRepository.save(post);

        Likes likes = likesRepository.save(Likes.create(member, post));

        //when
        Long deleteLikesId = likesService.unlike(loginMember.getId(), post.getId());

        //then
        assertThat(likes.getId()).isEqualTo(deleteLikesId);
        assertThrows(NoSuchElementException.class,() -> likesRepository.findById(deleteLikesId).orElseThrow(NoSuchElementException::new));
    }

    @Test
    void deleteByPostId() {
        //given
        Member tester1 = Member.create(Provider.create("KAKAOID", ProviderName.KAKAO), "tester1", MemberRole.MEMBER);
        Member tester2 = Member.create(Provider.create("KAKAOID", ProviderName.KAKAO), "tester2", MemberRole.MEMBER);
        memberRepository.save(tester1);
        memberRepository.save(tester2);
        Post post1 = Post.builder().customUrl(CustomUrl.create("1")).author(tester1).build();
        Post post2 = Post.builder().customUrl(CustomUrl.create("2")).author(tester1).build();
        postRepository.save(post1);
        postRepository.save(post2);
        likesRepository.save(Likes.create(tester1,post1));
        likesRepository.save(Likes.create(tester1,post2));
        likesRepository.save(Likes.create(tester2,post1));

        //when
        likesService.deleteByPostId(post1.getId());

        //then
        assertThat(likesRepository.findByPostId(post1.getId()).size()).isEqualTo(0);
    }

}
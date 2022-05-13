package jslog.likes.repository;

import jslog.likes.domain.Likes;
import jslog.member.auth.domain.Provider;
import jslog.member.auth.domain.ProviderName;
import jslog.member.member.domain.Member;
import jslog.member.member.domain.MemberRole;
import jslog.member.member.repository.MemberRepository;
import jslog.post.domain.Post;
import jslog.post.domain.url.CustomUrl;
import jslog.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class LikesRepositoryTest {

    @Autowired private PostRepository postRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private LikesRepository likesRepository;

    @Test
    @DisplayName("게시글의 좋아요 수 조회 성공")
    void countLikesByPostId() {
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
        int likes = likesRepository.countLikesByPostId(post1.getId());

        //then
        assertThat(likes).isEqualTo(2);
    }

    @Test
    @DisplayName("게시글 ID와 사용자 ID로 Likes 조회 성공")
    void findByPostIdAndMemberId() {
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
        Likes findLikes = likesRepository.findByMemberIdAndPostId(tester1.getId(), post1.getId()).orElseThrow(NoSuchElementException::new);

        //then
        assertThat(findLikes.getMember()).isEqualTo(tester1);
        assertThat(findLikes.getPost()).isEqualTo(post1);
    }

    @Test
    @DisplayName("게시글 ID로 Likes 조회 성공")
    void findByPostId() {
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
        List<Likes> likesList = likesRepository.findByPostId(post1.getId());

        //then
        assertThat(likesList.size()).isEqualTo(2);
    }
}
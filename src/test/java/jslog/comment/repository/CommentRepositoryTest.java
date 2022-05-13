package jslog.comment.repository;

import jslog.comment.domain.Comment;
import jslog.member.auth.domain.Provider;
import jslog.member.auth.domain.ProviderName;
import jslog.member.member.domain.Member;
import jslog.member.member.domain.MemberRole;
import jslog.member.member.repository.MemberRepository;
import jslog.post.domain.Post;
import jslog.post.domain.url.CustomUrl;
import jslog.post.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentRepositoryTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private CommentRepository commentRepository;

    @Test
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
        commentRepository.save(Comment.create(post1,tester1,"test1"));

        //when
        List<Comment> findComments = commentRepository.findByPostId(post1.getId());

        //then
        for (Comment findComment : findComments) {
            assertThat(findComment.getPost().getId()).isEqualTo(post1.getId());
        }
    }

}
package jslog.comment.application;

import jslog.comment.domain.Comment;
import jslog.comment.repository.CommentRepository;
import jslog.comment.ui.dto.CommentEditRequest;
import jslog.member.auth.domain.Provider;
import jslog.member.auth.domain.ProviderName;
import jslog.member.auth.exception.UnauthorizedException;
import jslog.member.auth.ui.LoginMember;
import jslog.member.member.domain.Member;
import jslog.member.member.domain.MemberRole;
import jslog.member.member.repository.MemberRepository;
import jslog.post.domain.Post;
import jslog.post.repository.PostRepository;
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
class CommentServiceTest {
    @Autowired private PostRepository postRepository;
    @Autowired private CommentService commentService;
    @Autowired private CommentRepository commentRepository;
    @Autowired private MemberRepository memberRepository;

    @Test
    @DisplayName("댓글 작성 성공")
    void writeTest() {
        //given
        Member member = Member.create(Provider.create("1", ProviderName.LOCAL), "tester1", MemberRole.MEMBER);
        memberRepository.save(member);
        Post post = Post.builder().title("1").build();
        postRepository.save(post);
        String commentContent = "test comment";

        //when
        Comment comment = commentService.write(commentContent, post.getId(), member.getId());

        //then
        assertThat(comment.getContent()).isEqualTo(commentContent);
        assertThat(comment.getPost().getTitle()).isEqualTo("1");
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void edit_success() {
        //given
        Member member = Member.create(Provider.create("1", ProviderName.LOCAL), "tester1", MemberRole.MEMBER);
        memberRepository.save(member);
        Post post = Post.builder().title("1").build();
        postRepository.save(post);
        String commentContent = "test comment";
        Comment comment = Comment.create(post,member,commentContent);
        commentRepository.save(comment);
        String editCommentContent = "edited test comment";
        CommentEditRequest commentEditRequest = new CommentEditRequest(comment.getId(),editCommentContent);
        LoginMember loginMember = LoginMember.createMember(member);

        //when
        commentService.edit(commentEditRequest, loginMember);

        //then
        assertThat(comment.getContent()).isEqualTo(editCommentContent);
    }

    @Test
    @DisplayName("댓글 수정 실패 - 인가되지 않은 접근")
    void edit_fail_unauthorizedAccess() {
        //given
        Member member1 = Member.create(Provider.create("1", ProviderName.LOCAL), "tester1", MemberRole.MEMBER);
        Member member2 = Member.create(Provider.create("2", ProviderName.LOCAL), "tester2", MemberRole.MEMBER);
        memberRepository.save(member1);
        memberRepository.save(member2);
        Post post = Post.builder().title("1").build();
        postRepository.save(post);
        String commentContent = "test comment";
        Comment comment = Comment.create(post,member1,commentContent);
        commentRepository.save(comment);
        String editCommentContent = "edited test comment";
        CommentEditRequest commentEditRequest = new CommentEditRequest(comment.getId(),editCommentContent);
        LoginMember loginMember = LoginMember.createMember(member2);

        //when
        //then
        assertThrows(UnauthorizedException.class, () -> commentService.edit(commentEditRequest, loginMember));
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteTest() {
        //given
        Member member = Member.create(Provider.create("1", ProviderName.LOCAL), "tester1", MemberRole.MEMBER);
        memberRepository.save(member);
        Post post = Post.builder().title("1").build();
        postRepository.save(post);
        String commentContent = "test comment";
        Comment comment = Comment.create(post,member,commentContent);
        commentRepository.save(comment);
        LoginMember loginMember = LoginMember.createMember(member);

        //when
        commentService.delete(comment.getId(),loginMember);

        //then
        assertThrows(NoSuchElementException.class, () -> commentRepository.findById(comment.getId()).orElseThrow(NoSuchElementException::new));
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 인가되지 않은 접근")
    void delete_fail_unauthorizedAccess() {
        //given
        Member member1 = Member.create(Provider.create("1", ProviderName.LOCAL), "tester1", MemberRole.MEMBER);
        Member member2 = Member.create(Provider.create("2", ProviderName.LOCAL), "tester2", MemberRole.MEMBER);
        memberRepository.save(member1);
        memberRepository.save(member2);
        Post post = Post.builder().title("1").build();
        postRepository.save(post);
        String commentContent = "test comment";
        Comment comment = Comment.create(post,member1,commentContent);
        commentRepository.save(comment);
        LoginMember loginMember = LoginMember.createMember(member2);

        //when
        //then
        assertThrows(UnauthorizedException.class, () -> commentService.delete(comment.getId(),loginMember));
    }

    @Test
    @DisplayName("게시글 ID로 해당 게시글에 등록되어있는 모든 댓글 삭제 성공")
    void deleteByPostId() {
        //given
        Member member1 = Member.create(Provider.create("1", ProviderName.LOCAL), "tester1", MemberRole.MEMBER);
        memberRepository.save(member1);
        Post post = Post.builder().title("1").build();
        postRepository.save(post);
        Comment comment = Comment.create(post,member1,"test comment");
        commentRepository.save(comment);

        //when
        commentService.deleteByPostId(post.getId());

        //then
        assertThat(commentRepository.findByPostId(post.getId()).size()).isEqualTo(0);
    }
}
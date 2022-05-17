package jslog.comment.application;

import jslog.comment.domain.Comment;
import jslog.comment.repository.CommentRepository;
import jslog.comment.ui.dto.CommentEditRequest;
import jslog.member.auth.exception.UnauthorizedException;
import jslog.member.auth.ui.LoginMember;
import jslog.member.member.domain.Member;
import jslog.member.member.domain.MemberRole;
import jslog.member.member.repository.MemberRepository;
import jslog.post.domain.Post;
import jslog.post.repository.PostRepository;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock CommentRepository commentRepository;
    @Mock MemberRepository memberRepository;
    @Mock PostRepository postRepository;
    CommentService commentService;

    Member member;
    LoginMember loginMember;
    Post post;
    Comment comment;

    @BeforeEach
    void setUp() {
        commentService = new CommentService(commentRepository,memberRepository,postRepository);

        member = Member.builder()
                .id(1L)
                .nickname("테스트유저")
                .memberRole(MemberRole.MEMBER)
                .build();

        loginMember = LoginMember.createMember(member);

        post = Post.builder().id(1L)
                .author(member)
                .title("첫번째 게시글 제목!!")
                .content("첫번째 게시글 내용!!!")
                .build();

        comment = Comment.create(post, member, "댓글 내용!!");
    }

    @Test
    @DisplayName("댓글을 작성한다.")
    void write() {
        when(postRepository.findById(any())).thenReturn(Optional.of(post));
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(commentRepository.save(any())).thenReturn(comment);

        commentService.write(comment.getContent(), post.getId(), member.getId());

        verify(commentRepository).save(any());
    }

    @Test
    @DisplayName("댓글을 수정한다.")
    void edit() {
        CommentEditRequest commentEditRequest = new CommentEditRequest(comment.getId(), "수정된 댓글 내용!!");
        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

        commentService.edit(commentEditRequest, loginMember);

        assertThat(comment.getContent()).isEqualTo(commentEditRequest.getContent());
    }

    @Test
    @DisplayName("어드민이 아닌 악성 사용자가 다른 사람의 댓글을 수정하려 한다. - 수정 실패")
    void edit_fail_cause_by_unauthorized_access() {
        CommentEditRequest commentEditRequest = new CommentEditRequest(comment.getId(), "수정된 댓글 내용!!");
        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

        LoginMember anotherLoginMember = LoginMember.createMember(2L,"다른 유저", MemberRole.MEMBER);

        assertThrows(UnauthorizedException.class, () -> commentService.edit(commentEditRequest, anotherLoginMember));
    }

    @Test
    @DisplayName("댓글을 삭제한다.")
    void delete() {
        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

        commentService.delete(comment.getId(),loginMember);

        verify(commentRepository).delete(any());
    }

    @Test
    @DisplayName("어드민이 아닌 악성 사용자가 다른 사람의 댓글을 삭제하려 한다. - 삭제 실패")
    void delete_fail_cause_by_unauthorized_access() {
        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

        LoginMember anotherLoginMember = LoginMember.createMember(2L,"다른 유저", MemberRole.MEMBER);

        assertThrows(UnauthorizedException.class, () -> commentService.delete(comment.getId(),anotherLoginMember));
    }

    @Test
    @DisplayName("게시글에 등록된 모든 댓글들을 삭제한다.")
    void deleteCommentsByPostId() {
        when(commentRepository.findByPostId(any())).thenReturn(new ArrayList<>(Arrays.asList(comment)));

        commentService.deleteCommentsByPostId(post.getId());

        verify(commentRepository).delete(comment);
    }
}
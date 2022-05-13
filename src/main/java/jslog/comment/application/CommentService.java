package jslog.comment.application;

import jslog.comment.domain.Comment;
import jslog.comment.repository.CommentRepository;
import jslog.comment.ui.dto.CommentDto;
import jslog.comment.ui.dto.CommentEditForm;
import jslog.member.auth.exception.UnauthorizedException;
import jslog.member.auth.ui.LoginMember;
import jslog.member.member.domain.MemberRole;
import jslog.member.member.repository.MemberRepository;
import jslog.post.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public List<CommentDto> getCommentDtoListByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(CommentDto::create).collect(Collectors.toList());
    }

    @Transactional
    public Comment write(String comment, Long postId, Long AuthorId) {
        return commentRepository.save(
                Comment.create(
                        postRepository.findById(postId).orElseThrow(NoSuchElementException::new),
                        memberRepository.findById(AuthorId).orElseThrow(NoSuchElementException::new),
                        comment)
        );
    }

    @Transactional
    public void edit(CommentEditForm commentEditForm, LoginMember loginMember) {
        Comment findComment = commentRepository.findById(commentEditForm.getCommentId())
                .orElseThrow(NoSuchElementException::new);

        checkAuthorization(findComment.getAuthor().getId(), loginMember);

        findComment.edit(commentEditForm);
    }

    @Transactional
    public void deleteByPostId(Long postId) {
        for (Comment comment : commentRepository.findByPostId(postId)) {
            commentRepository.delete(comment);
        }
    }

    @Transactional
    public void delete(Long commentId, LoginMember loginMember) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(NoSuchElementException::new);

        checkAuthorization(findComment.getAuthor().getId(), loginMember);

        commentRepository.delete(findComment);
    }

    private void checkAuthorization(Long authorizedId, LoginMember loginMember) {
        if (!loginMember.getId().equals(authorizedId) && !loginMember.getMemberRole().equals(MemberRole.ADMIN))
            throw new UnauthorizedException();
    }
}

package jslog.comment.ui;

import jslog.comment.application.CommentService;
import jslog.comment.domain.Comment;
import jslog.comment.repository.CommentRepository;
import jslog.comment.ui.dto.CommentEditForm;
import jslog.commons.SessionConst;
import jslog.member.auth.ui.LoginMember;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentRepository commentRepository;
    private final CommentService commentService;

    @PostMapping()
    public String write(@RequestParam(name = "redirectUrl") String redirectUrl,
                        @RequestParam(name = "postId") Long postId,
                        @RequestParam(name = "authorId") Long authorId,
                        @RequestParam(name = "comment") String comment) {

        commentService.write(comment,postId,authorId);

        return "redirect:/posts/"+redirectUrl;
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam(name = "commentId") Long commentId,
                           @RequestParam(name = "redirectUrl") String redirectUrl,
                           @ModelAttribute CommentEditForm commentEditForm,
                           Model model) {

        Comment findComment = commentRepository.findById(commentId).orElseThrow(NoSuchElementException::new);

        commentEditForm.setCommentId(commentId);
        commentEditForm.setContent(findComment.getContent());

        model.addAttribute("redirectUrl", redirectUrl);

        return "blog/comment/edit";
    }

    @PostMapping("/edit")
    public String edit(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                       @RequestParam(name = "redirectUrl") String redirectUrl,
                       @ModelAttribute CommentEditForm commentEditForm) {

        commentService.edit(commentEditForm, loginMember);

        return "redirect:/"+redirectUrl;
    }

    @PostMapping("/delete")
    public String delete(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                       @RequestParam(name = "redirectUrl") String redirectUrl,
                       @RequestParam(name = "commentId") Long commentId) {

        commentService.delete(commentId, loginMember);

        return "redirect:/"+redirectUrl;
    }



}

package jslog.comment.ui;

import jslog.comment.application.CommentService;
import jslog.comment.domain.Comment;
import jslog.comment.repository.CommentRepository;
import jslog.comment.ui.dto.CommentEditRequest;
import jslog.commons.SessionConst;
import jslog.member.auth.ui.LoginMember;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentRepository commentRepository;
    private final CommentService commentService;

    @PostMapping()
    public String write(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                        @RequestParam(name = "redirectUrl") String redirectUrl,
                        @RequestParam(name = "redirectAuthorId") Long redirectAuthorId,
                        @RequestParam(name = "postId") Long postId,
                        @RequestParam(name = "comment") String comment) {

        commentService.write(comment,postId,loginMember);

        return "redirect:/posts/"+redirectAuthorId+"/"+ URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8);
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam(name = "commentId") Long commentId,
                           @RequestParam(name = "redirectUrl") String redirectUrl,
                           @RequestParam(name = "redirectAuthorId") Long redirectAuthorId,
                           @ModelAttribute CommentEditRequest commentEditRequest,
                           Model model) {

        Comment findComment = commentRepository.findById(commentId).orElseThrow(NoSuchElementException::new);

        commentEditRequest.setCommentId(commentId);
        commentEditRequest.setContent(findComment.getContent());

        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("redirectAuthorId", redirectAuthorId);
        
        return "blog/comment/edit";
    }

    @PostMapping("/edit")
    public String edit(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                       @RequestParam(name = "redirectUrl") String redirectUrl,
                       @RequestParam(name = "redirectAuthorId") Long redirectAuthorId,
                       @ModelAttribute CommentEditRequest commentEditRequest) {

        commentService.edit(commentEditRequest, loginMember);

        return "redirect:/posts/"+redirectAuthorId+"/"+URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8);
    }

    @PostMapping("/delete")
    public String delete(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                       @RequestParam(name = "redirectUrl") String redirectUrl,
                         @RequestParam(name = "redirectAuthorId") Long redirectAuthorId,
                       @RequestParam(name = "commentId") Long commentId) {

        commentService.delete(commentId, loginMember);

        return "redirect:/posts/"+redirectAuthorId+"/"+URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8);
    }



}

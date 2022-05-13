package jslog.likes.ui;

import jslog.commons.SessionConst;
import jslog.likes.application.LikesService;
import jslog.member.auth.ui.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @GetMapping("/like")
    public String like(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                       @RequestParam(name = "postId") Long postId,
                       @RequestParam(name = "redirectUrl") String redirectUrl) {

        likesService.create(loginMember.getId(),postId);

        return "redirect:"+redirectUrl;
    }

    @GetMapping("/unlike")
    public String unlike(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                         @RequestParam(name = "postId") Long postId,
                         @RequestParam(name = "redirectUrl") String redirectUrl) {

        likesService.delete(loginMember.getId(), postId);

        return "redirect:"+redirectUrl;
    }
}

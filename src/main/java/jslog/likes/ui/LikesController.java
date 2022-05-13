package jslog.likes.ui;

import jslog.commons.SessionConst;
import jslog.likes.application.LikesService;
import jslog.member.auth.ui.LoginMember;
import jslog.post.domain.url.CustomUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @GetMapping("/like")
    public String like(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                       @RequestParam(name = "postId") Long postId,
                       @RequestParam(name = "redirectAuthorId") Long redirectAuthorId,
                       @RequestParam(name = "redirectUrl") String redirectUrl) {

        likesService.like(loginMember.getId(),postId);

        return "redirect:/posts/"+redirectAuthorId+"/"+URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8);
    }

    @GetMapping("/unlike")
    public String unlike(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                         @RequestParam(name = "postId") Long postId,
                         @RequestParam(name = "redirectAuthorId") Long redirectAuthorId,
                         @RequestParam(name = "redirectUrl") String redirectUrl) {

        likesService.unlike(loginMember.getId(), postId);
        
        return "redirect:/posts/"+redirectAuthorId+"/"+URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8);
    }
}

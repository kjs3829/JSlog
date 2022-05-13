package jslog.post.ui;

import jslog.member.auth.ui.LoginMember;
import jslog.post.application.PostService;
import jslog.post.domain.Post;
import jslog.post.domain.url.CustomUrl;
import jslog.post.repository.PostRepository;
import jslog.post.ui.dto.PostEditForm;
import jslog.post.ui.dto.PostWriteForm;
import jslog.commons.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * TODO : EditForm, DeleteForm의 id를 분리시켜 RESTful 하게 변경해야됨, postService에 edit 로직 추가해야함
 *
 */
@Controller @Slf4j
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostRepository postRepository;
    private final PostService postService;

    @GetMapping("/{authorId}")
    public String getMemberBlogHome(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) LoginMember loginMember,
                                    @PathVariable Long authorId,
                                    @RequestParam(required = false) String q,
                                    @RequestParam(required = false) String tag,
                                    @RequestParam(required = false) Integer p,
                                    Model model) {

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("authorId", authorId);
        model.addAttribute("memberTags", postService.getMemberTags(authorId));
        model.addAttribute("page", postService.getPageSelector(authorId, q, tag, p));

        return "blog/home";
    }

    @GetMapping("/{authorId}/{url}")
    public String getPost(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) LoginMember loginMember,
                                    @PathVariable Long authorId,
                                    @PathVariable String url,
                                    Model model) {

        model.addAttribute("post", postService.getPostReadForm(authorId, url, loginMember));
        model.addAttribute("loginMember", loginMember);
        return "blog/post";
    }

    @GetMapping("/write")
    public String getWritePage(@ModelAttribute PostWriteForm postWriteForm) {
        return "blog/write";
    }

    // TODO : 인가 프로세스 추가해야됨
    @GetMapping("/edit")
    public String getEditPage(@RequestParam Long postId, @ModelAttribute PostEditForm postEditForm) {

        Post findPost = postRepository.findById(postId).orElse(null);

        postEditForm.setId(postId);
        postEditForm.setTitle(findPost.getTitle());
        postEditForm.setContent(findPost.getContent());
        postEditForm.setUrl(findPost.getStringUrl());
        postEditForm.setTags(findPost.getStringTags());
        postEditForm.setPreview(findPost.getPreview());

        return "blog/edit";
    }

    @PostMapping("")
    public String write(@Valid @ModelAttribute PostWriteForm form, BindingResult bindingResult,
                        @SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMember loginMember) {

        form.setUrl(CustomUrl.create(form.getUrl()).getUrl());

        if (postService.isDuplicatedUrl(loginMember.getId(),form.getUrl())) {
            bindingResult.reject("duplicatedUrl", "이미 존재하는 url 입니다.");
            return "blog/write";
        }

        postService.createPost(form, loginMember);

        return "redirect:/posts/"+loginMember.getId()+"/"+ URLEncoder.encode(CustomUrl.create(form.getUrl()).getUrl(), StandardCharsets.UTF_8);
    }

    @PostMapping("/edit")
    public String edit(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                           @Valid @ModelAttribute PostEditForm postEditForm,
                           BindingResult bindingResult) {

        if (!postService.updatePost(postEditForm,loginMember)) {
            bindingResult.reject("duplicatedUrl", "이미 존재하는 url 입니다.");
            return "blog/edit";
        }

        return "redirect:/posts/"+loginMember.getId()+"/"+ URLEncoder.encode(CustomUrl.create(postEditForm.getUrl()).getUrl(), StandardCharsets.UTF_8);
    }

    @PostMapping("/delete")
    public String deletePost(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                             @RequestParam Long postId){
        Long authorId = postService.delete(loginMember, postId);

        return "redirect:/posts/" + authorId;
    }
}

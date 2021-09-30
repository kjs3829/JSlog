package jslog.web;

import jslog.domain.MemberRepository;
import jslog.domain.member.Member;
import jslog.domain.member.MemberRole;
import jslog.domain.post.PostRepository;
import jslog.domain.post.PostService;
import jslog.domain.post.entity.Post;
import jslog.domain.post.form.PostDeleteForm;
import jslog.domain.post.form.PostEditForm;
import jslog.domain.post.form.PostReadForm;
import jslog.domain.post.form.PostWriteForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller @Slf4j
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostRepository postRepository;
    private final PostService postService;

    /*
        TODO:
         post.html 수정
         post CRUD Controller 개발
     */

    @GetMapping("")
    @ResponseBody
    public List<Post> postList() {
        return postRepository.findAll();
    }

    @GetMapping("/{url}")
    public String getPostByUrl(@PathVariable String url, Model model, HttpServletRequest request) {
        Post findPost = postRepository.findByUrl(url);

        // url에 해당하는 post가 없을 경우
        if (findPost == null) {
            return "redirect:/blog";
        }

        PostReadForm post = postService.postToReadForm(findPost);

        model.addAttribute("post", post);

        HttpSession session = request.getSession(false);

        if (session != null && findPost.getAuthor().getId().equals(((Member) session.getAttribute(SessionConst.LOGIN_MEMBER)).getId())) {
            PostDeleteForm p = new PostDeleteForm(findPost.getId());
            model.addAttribute("postDeleteForm", p);
            log.info("SinglePost post.id = {}", post.getId());
            return "blog/post-admin";
        }
        log.info("SinglePost post.id = {}", post.getId());
        return "blog/post";
    }

    @GetMapping("/write")
    public String postWriteForm(@ModelAttribute PostWriteForm postWriteForm) {
        return "blog/write";
    }

    @PostMapping("")
    public String writePost(@Valid @ModelAttribute PostWriteForm postWriteForm, BindingResult bindingResult,
                            HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        String postUrl = postWriteForm.getUrl();
        // 게시글 url 중복 검사
        if (postRepository.hasUrl(postUrl)) {
            bindingResult.reject("duplicatedUrl", "이미 존재하는 url 입니다.");
        }

        if (bindingResult.hasErrors()) {
            return "blog/write";
        }

        postWriteForm.setAuthor((Member) session.getAttribute(SessionConst.LOGIN_MEMBER));
        postService.write(postWriteForm);
        return "redirect:/posts/" + postUrl;
    }

    @GetMapping("/edit")
    public String postEditForm(@RequestParam Long id, @ModelAttribute PostEditForm postEditForm, HttpServletResponse response) {
        Post findPost = postRepository.findById(id);

        // TODO : 존재하지 않는 포스트를 수정하려는 경우
        if (findPost == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

        postEditForm.setId(id);
        postEditForm.setTitle(findPost.getTitle());
        postEditForm.setContent(findPost.getContent());
        postEditForm.setUrl(findPost.getUrl());
        postEditForm.setPreview(findPost.getPreview());

        return "blog/edit";
    }

    @PostMapping("/edit")
    public String editPost(@Valid @ModelAttribute PostEditForm postEditForm, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long postId = postEditForm.getId();
        Post findPost = postRepository.findById(postId);
        // TODO : 존재하지 않는 포스트를 수정하려는 경우
        if (findPost == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        String postUrl = postEditForm.getUrl();
        // 게시글 url 중복 검사
        if (postRepository.hasUrl(postUrl) && !findPost.getUrl().equals(postEditForm.getUrl())) {
            bindingResult.reject("duplicatedUrl", "이미 존재하는 url 입니다.");
            return "blog/edit";
        }

        if (bindingResult.hasErrors()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:/blog";
        }

        postRepository.update(postId, postEditForm);
        return "redirect:/posts/" + postEditForm.getUrl();
    }

    @DeleteMapping("/delete")
    public String deletePost(@ModelAttribute PostDeleteForm postDeleteForm, HttpServletRequest request, HttpServletResponse response) throws IOException {

        Long deletePostId = postDeleteForm.getId();
        log.info("DeletePostId = {}", deletePostId);
        // TODO : 존재하지 않는 포스트를 삭제하려는 경우
        if (!postRepository.hasId(deletePostId)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        postService.delete(deletePostId);
        log.info("ID = {} Post Deleted.", deletePostId);
        return "redirect:/blog";
    }
}

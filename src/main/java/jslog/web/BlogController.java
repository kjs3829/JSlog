package jslog.web;

import jslog.domain.BlogService;
import jslog.domain.TagRepository;
import jslog.domain.member.entity.Member;
import jslog.domain.post.PageSelector;
import jslog.domain.post.PostRepository;
import jslog.domain.post.entity.Post;
import jslog.domain.post.entity.Tag;
import jslog.domain.post.form.PostSelectForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/blog")
@RequiredArgsConstructor
@Slf4j
public class BlogController {
    private final BlogService blogService;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    @GetMapping("")
    public String blogHome(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member,
                           @RequestParam(required = false) Integer page,
                           Model model) {

        if (page == null) {
            page = 1;
        }

        List<PostSelectForm> posts = blogService.getPage(page);
        PageSelector pageSelector = new PageSelector(page, postRepository.getMaxPage());
        log.info("PageSelector = {}", pageSelector);
        //없는 페이지를 요청했을 경우
        if (posts == null) {
            return "redirect:/blog";
        }

        List<Tag> tags = tagRepository.findAll();
        model.addAttribute("tags", tags);
        model.addAttribute("pageSelector", pageSelector);
        model.addAttribute("posts", posts);
        model.addAttribute("loginMember", member);
        return "blog";
    }

    @GetMapping("/{tag}")
    public String tagHome(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member,
                          @PathVariable(name = "tag") String tag,
                          Model model) {
        List<Post> posts = tagRepository.findPostByTag(tag);
        List<PostSelectForm> selectForms = new ArrayList<>();
        for (Post post : posts) {
            selectForms.add(post.toSelectForm());
        }
        model.addAttribute("posts", selectForms);

        List<Tag> tags = tagRepository.findAll();
        model.addAttribute("tags", tags);
        model.addAttribute("tag", tag);
        model.addAttribute("loginMember", member);

        return "blog-tag";
    }
}

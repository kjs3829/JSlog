package com.jslog.blog.web;

import com.jslog.blog.domain.post.PageSelector;
import com.jslog.blog.domain.post.Post;
import com.jslog.blog.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/blog")
@RequiredArgsConstructor
@Slf4j
public class BlogController {
    public final PostRepository postRepository;

    @GetMapping("")
    public String blogHome(@RequestParam(required = false) Integer page, Model model) {
        if (page == null) {
            page = 1;
        }
        List<Post> posts = postRepository.getPage(page);

        //없는 페이지를 요청했을 경우
        if (posts == null) {
            return "redirect:/blog";
        }

        model.addAttribute("pageSelector", new PageSelector(page, postRepository.getMaxPage()));
        model.addAttribute("posts", posts);
        return "blog";
    }
}

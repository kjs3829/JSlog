package com.jslog.blog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Navigation 이 제공하는 URL들을 매핑하는 Home Controller
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request) {
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/portfolio")
    public String portfolio() {
        return "portfolio";
    }

    @GetMapping("/blog")
    public String blog() {
        return "blog";
    }

}

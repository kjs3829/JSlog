package jslog.web;

import jslog.web.interceptor.AdminCheckInterceptor;
import jslog.web.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/posts/write","/posts/edit","/posts/delete");
        registry.addInterceptor(new AdminCheckInterceptor())
                .order(2)
                .addPathPatterns("/posts/write","/posts/edit","/posts/delete");
    }
}

package com.mile.config.web;

import com.mile.resolver.comment.CommentVariableResolver;
import com.mile.resolver.moim.MoimVariableResolver;
import com.mile.resolver.post.PostVariableResolver;
import com.mile.resolver.reply.ReplyVariableResolver;
import com.mile.resolver.topic.TopicVariableResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final MoimVariableResolver moimVariableResolver;
    private final TopicVariableResolver topicVariableResolver;
    private final PostVariableResolver postVariableResolver;
    private final CommentVariableResolver commentVariableResolver;
    private final ReplyVariableResolver replyVariableResolver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
                .maxAge(3000);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(moimVariableResolver);
        resolvers.add(topicVariableResolver);
        resolvers.add(commentVariableResolver);
        resolvers.add(postVariableResolver);
        resolvers.add(replyVariableResolver);
    }
}
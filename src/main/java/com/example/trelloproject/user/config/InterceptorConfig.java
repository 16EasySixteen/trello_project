package com.example.trelloproject.user.config;

import com.example.trelloproject.user.interceptor.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private static final String[] WSADMIN_ROLE_REQUIRED_PATH_PATTERNS = {"/workspaces/*/invitation"};
    private static final String[] MEMBER_ROLE_REQUIRED_PATH_PATTERNS = {"/workspaces/*/invitation"};
    private static final String[] ONLYREAD_ROLE_REQUIRED_PATH_PATTERNS = {"/workspaces/**"};

    private final WsadminAuthInterceptor wsadminAuthInterceptor;
    private final MemberAuthInterceptor memberAuthInterceptor;
    private final OnlyreadAuthInterceptor onlyreadAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(wsadminAuthInterceptor)
                .addPathPatterns(WSADMIN_ROLE_REQUIRED_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE);

        registry.addInterceptor(memberAuthInterceptor)
                .addPathPatterns(MEMBER_ROLE_REQUIRED_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE + 1);

        registry.addInterceptor(onlyreadAuthInterceptor)
                .addPathPatterns(ONLYREAD_ROLE_REQUIRED_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE + 2);
    }
}

package com.example.trelloproject.user.config.filter;

import com.example.trelloproject.user.util.AuthenticationScheme;
import com.example.trelloproject.user.util.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        this.authenticate(request);
        filterChain.doFilter(request, response);
    }

    // request를 이용한 인증 처리
    private void authenticate(HttpServletRequest request) {
        log.info("인증 처리");

        String token = this.getTokenFromRequest(request);
        if(!jwtProvider.validateToken(token)) {
            return;
        }

        String username = jwtProvider.getUsername(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        this.setAuthentication(request, userDetails);
    }

    // request의 Authentication 헤더에서 토큰 값 추출
    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String headerPrefix = AuthenticationScheme.generateType(AuthenticationScheme.BEARER);

        boolean tokenFound = StringUtils.hasText(bearerToken) && bearerToken.startsWith(headerPrefix);

        if(tokenFound) {
            return bearerToken.substring(headerPrefix.length());
        }

        return null;
    }

    // SecurityContext에 인증 객체 저장
    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

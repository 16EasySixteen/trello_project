package com.example.trelloproject.user.interceptor;

import com.example.trelloproject.user.entity.User;
import com.example.trelloproject.user.entity.UserWorkspace;
import com.example.trelloproject.user.enumclass.MemberRole;
import com.example.trelloproject.user.exception.ForbiddenException;
import com.example.trelloproject.user.repository.UserRepository;
import com.example.trelloproject.user.util.JwtProvider;
import com.example.trelloproject.user.util.PathVariableExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WsadminAuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ResponseStatusException {

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            log.info("재가공한 토큰 : {}", token);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");
        }

        if (jwtProvider.validateToken(token)) {
            String username = jwtProvider.getUsername(token);

            // memberRole이 null값일 때
            User user = userRepository.findByEmail(username).orElseThrow(() -> new ForbiddenException("접근 권한이 없습니다."));
            Long workspaceId = PathVariableExtractor.extractPathVariable(request, "workspaceId");

            List<UserWorkspace> userWorkspace = user.getUserWorkspace().stream().filter(uw -> uw.getWorkspace().getWorkspaceId().equals(workspaceId)).toList();
            MemberRole memberRole = userWorkspace.get(0).getMemberRole();

            if (memberRole.equals(MemberRole.WSADMIN)) {
                return true;
            }
            throw new ForbiddenException("접근 권한이 없습니다.");

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }
    }
}

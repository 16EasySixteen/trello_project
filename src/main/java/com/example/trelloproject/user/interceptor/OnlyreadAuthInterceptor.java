package com.example.trelloproject.user.interceptor;

import com.example.trelloproject.user.entity.User;
import com.example.trelloproject.user.entity.UserWorkspace;
import com.example.trelloproject.user.enumclass.MemberRole;
import com.example.trelloproject.user.enumclass.UserRole;
import com.example.trelloproject.user.exception.ForbiddenException;
import com.example.trelloproject.user.repository.UserRepository;
import com.example.trelloproject.user.util.JwtProvider;
import com.example.trelloproject.user.util.PathVariableExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.management.relation.Role;
import java.util.List;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@Component
@RequiredArgsConstructor
@Slf4j
public class OnlyreadAuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ResponseStatusException {

        String path = request.getRequestURI();
        String method = request.getMethod();
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
            if(workspaceId.equals(0L) && "POST".equals(request.getMethod()) && user.getUserRole().equals(UserRole.ADMIN)){
                return true;
            }
            List<UserWorkspace> userWorkspace = user.getUserWorkspace().stream().filter(uw -> uw.getWorkspace().getWorkspaceId().equals(workspaceId)).toList();
            MemberRole memberRole = userWorkspace.get(0).getMemberRole();

            if (!memberRole.equals(MemberRole.ONLYREAD)) {
                return true;
            }

            if("GET".equals(request.getMethod())) {
                return true;
            }

            // OnlyRead 권한이 있을 때 PATCH /workspaces/{workspaceId}/invitation/{invitationId} 요청은 허용
            if (HttpMethod.PATCH.matches(method) && path.matches("/workspaces/\\d+/invitation/\\d+")) {
                return true; // 허용
            }

            throw new ForbiddenException("접근 권한이 없습니다.");

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }
    }
}

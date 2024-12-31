package com.example.trelloproject.user.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.access.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {

        Map<String, String> response = new HashMap<>();
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        response.put("message", message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Map<String, String>> handleAuthException(AuthenticationException e) {

        HttpStatus statusCode = e instanceof BadCredentialsException
                ? HttpStatus.FORBIDDEN
                : HttpStatus.UNAUTHORIZED;

        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());

        return ResponseEntity.status(statusCode).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException e) {

        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    protected ResponseEntity<Map<String, String>> handleAuthorizationDeniedException(AuthorizationDeniedException e) {

        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<Map<String, String>> handleJwtException(JwtException e) {
        HttpStatus httpStatus = e instanceof ExpiredJwtException
                ? HttpStatus.UNAUTHORIZED
                : HttpStatus.FORBIDDEN;

        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());

        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatusExceptions(ResponseStatusException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error code", e.getStatusCode().toString());
        response.put("message", e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).body(response);
    }

    //인터셉터에서 발생한 권한관련 접근금지 예외처리
    @ExceptionHandler(ForbiddenException.class)
    protected ResponseEntity<Map<String, String>> handleForbiddenException(ForbiddenException e) {

        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Map<String, String>> handleGlobalException(Exception e) {
        log.error("Unhandled exception: {}", e.getMessage(), e);

        Map<String, String> response = new HashMap<>();
        response.put("message", "An unexpected error occurred");
        response.put("error", e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

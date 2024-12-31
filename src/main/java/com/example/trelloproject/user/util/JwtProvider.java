package com.example.trelloproject.user.util;

import ch.qos.logback.core.util.StringUtil;
import com.example.trelloproject.user.entity.User;
import com.example.trelloproject.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Getter
    @Value("300000")
    private long expiration;

    private final UserRepository userRepository;

    // 토큰 생성 후 리턴
    public String generateToken(Authentication authentication) throws EntityNotFoundException {
        String username = authentication.getName();
        return generateTokenBy(username);
    }

    // 토큰에서 username 리턴
    public String getUsername(String token) {
        Claims claims = this.getClaims(token);
        return claims.getSubject();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) throws JwtException {
        try {
            return !this.tokenExpired(token);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token", e.getMessage());
        }

        return false;
    }

    // 이메일 주소로 토큰을 생성 후 리턴
    private String generateTokenBy(String email) throws EntityNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("해당 이메일이 존재하지 않습니다."));
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + expiration);

        //HS256 알고리즘을 이용하여 토큰 생성
        return Jwts.builder()
            .subject(email)
            .issuedAt(currentDate)
            .expiration(expirationDate)
            .claim("userRole", user.getUserRole())
            .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
            .compact();
    }

    // JWT의 claim 추출
    private Claims getClaims(String token) {
        if(!StringUtils.hasText(token)) {
            throw new MalformedJwtException("Invalid JWT token");
        }

        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 토큰 만료 여부 확인
    private boolean tokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //토큰의 만료일 반환
    private Date getExpirationDateFromToken(String token) {
        return resolveClaims(token, Claims::getExpiration);
    }

    // 토큰에 로직 적용 후 리턴
    private <T> T resolveClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }
}

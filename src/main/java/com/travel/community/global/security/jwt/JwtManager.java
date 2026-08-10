package com.travel.community.global.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.travel.community.global.exception.BusinessException;
import com.travel.community.global.security.jwt.exception.JwtErrorCode;
import com.travel.community.pages.oauth.enums.ProviderType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtManager {

    @Value("${jwt.name}")
    private String JWT_NAME; // jwt 토큰 이름

    @Value("${jwt.expired-time}")
    private Integer EXPIRED_TIME;

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String createJwt(String userId, ProviderType type) {
        Date now = new Date();
        return Jwts.builder()
                .subject(userId)
                .claim("provider", type.name())
                .issuedAt(now)
                .expiration(new Date(System.currentTimeMillis() + EXPIRED_TIME))
                .signWith(key)
                .compact();
    }

    public String getJwt(HttpServletRequest request) {
        return Optional.ofNullable(WebUtils.getCookie(request, JWT_NAME)).map(Cookie::getValue)
                .orElseThrow(() -> new BusinessException(JwtErrorCode.INVALID_JWT));
    }

    /**
     * JWT 유효성 검증과 동시에 user id 반환
     * 
     * @return
     * @throws Exception
     */
    public String getUserId(String jwtToken) {
        // 헤더에서 JWT 추출
        if (jwtToken == null || jwtToken.length() == 0) {
            return null;
        }

        try {
            // jwt 토큰에서 claims 추출
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwtToken).getPayload();

            return claims.getSubject();

        } catch (ExpiredJwtException e) {
            log.warn("토큰 만료: {}", e.getMessage());
            throw new BusinessException(JwtErrorCode.EXPIRED_JWT);

        } catch (MalformedJwtException | UnsupportedJwtException | DecodingException | IllegalArgumentException e) {
            log.warn("토큰 파싱 실패: {}", e.getMessage());
            throw new BusinessException(JwtErrorCode.INVALID_JWT);

        } catch (Exception e) {
            log.error("원인을 알 수 없는 토큰 파싱 실패: {}", e.getMessage());
            throw new BusinessException(JwtErrorCode.INVALID_JWT);
        }

    }

    /**
     * 쿠키 삭제
     *
     * @return
     */
    public void getCookieToDelete(HttpServletResponse response) {
        Cookie cookie = new Cookie(JWT_NAME, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * jwt를 쿠키에 저장
     * 
     * @param response
     * @param token
     */
    public void addCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(JWT_NAME, token);
        cookie.setHttpOnly(true); // true면 클라이언트에서 쿠키에 접근할 수 있음
        cookie.setSecure(false);// 개발환경, 운영은 true(true는 https일때만 쿠키를 request에 자동으로 실어서 보냄, false는 http, https 둘다
        // 보냄)
        cookie.setPath("/"); // 어떤 url에 쿠키를 실어서 요청 보낼건지 (/면 모든 요청)
        cookie.setMaxAge(EXPIRED_TIME); // 쿠키 만료시간

        response.addCookie(cookie);
    }
}

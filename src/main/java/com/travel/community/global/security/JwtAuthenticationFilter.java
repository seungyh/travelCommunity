package com.travel.community.global.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.travel.community.global.security.jwt.JwtManager;
import com.travel.community.global.security.jwt.exception.CustomAuthenticationEntryPoint;
import com.travel.community.global.security.jwt.exception.JwtErrorCode;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProtectedRequestMatcher protectedRequestMatcher;
    private final JwtManager jwtManager;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !protectedRequestMatcher.matches(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            String userId = jwtManager.getUserId(request);
            if (!StringUtils.hasText(userId)) {
                throw new BadCredentialsException(JwtErrorCode.INVALID_JWT.getErrorMessage());
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            jwtManager.refreshJwtCookie(request, response);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            jwtManager.getCookieToDelete(response);
            customAuthenticationEntryPoint.commence(request, response,
                    new BadCredentialsException(JwtErrorCode.INVALID_JWT.getErrorMessage(), e));
            return;
        }

        // 패턴이 여러 개 일치해도 인증/쿠키 갱신/다음 필터 호출은 한 번만 수행합니다.
        filterChain.doFilter(request, response);
    }
}
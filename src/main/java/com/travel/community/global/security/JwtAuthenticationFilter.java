package com.travel.community.global.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
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

	@Value("${filter.exclude-address}")
	private List<String> EXCLUDE_ADDRESS;

	private final AntPathMatcher pathMatcher = new AntPathMatcher();
	private final JwtManager jwtManager;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	// 로그인 정보 필요한 기능은 token filter에서 검사
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String uri = request.getRequestURI();

		for (String pattern : EXCLUDE_ADDRESS) {
			if (pathMatcher.match(pattern, uri)) {
				filterChain.doFilter(request, response); // 검증 로직을 건너뛰고 다음 필터로 진행
				return;
			}
		}
		String userId = null;
		try {
			String token = jwtManager.getJwt(request);
			userId = jwtManager.getUserId(token);

			// spring security 권한 인증 객체 ROLE_USER
			List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					userId,
					null,
					authorities);
			SecurityContextHolder.getContext()
					.setAuthentication(authentication);

		} catch (Exception e) {
			tokenDeleteProcess(response);
			SecurityContextHolder.clearContext();

			customAuthenticationEntryPoint.commence(
					request,
					response,
					new BadCredentialsException(
							JwtErrorCode.INVALID_JWT.getErrorMessage(),
							e));

			return;

		}

		// userId 미존재
		if (userId != null) {
			filterChain.doFilter(request, response);
		} else {
			tokenDeleteProcess(response);
			customAuthenticationEntryPoint.commence(
					request,
					response,
					new BadCredentialsException(
							JwtErrorCode.INVALID_JWT.getErrorMessage(),
							null));

			return;

		}

	}

	// 토큰 삭제 및 에러 response에 저장
	private void tokenDeleteProcess(HttpServletResponse response) throws IOException {

		// 검증 실패: 필터 단에서 즉시 401 Unauthorized 응답 반환
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpResponse.setContentType("application/json;charset=UTF-8");
		jwtManager.getCookieToDelete(response);
	}

}

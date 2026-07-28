package com.travel.community.global.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.travel.community.global.security.jwt.JwtManager;
import com.travel.community.global.security.jwt.dto.TokenDto;

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
		TokenDto tokenDto = null;
		try {
			String token = jwtManager.getJwt(request);
			tokenDto = jwtManager.getTokenDto(token);

		} catch (Exception e) {

		}

		if (tokenDto.getUserId() != null) {
			filterChain.doFilter(request, response);
		} else {
			// 검증 실패: 필터 단에서 즉시 401 Unauthorized 응답 반환
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.setContentType("application/json;charset=UTF-8");
			httpResponse.getWriter().write("{\"error\": \"Invalid or Expired Token\"}");
			jwtManager.getCookieToDelete(response);
		}

	}

}

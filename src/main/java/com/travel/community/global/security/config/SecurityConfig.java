package com.travel.community.global.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import com.travel.community.global.security.JwtAuthenticationFilter;
import com.travel.community.global.security.jwt.exception.CustomAuthenticationEntryPoint;
import com.travel.community.pages.oauth.service.CustomOAuth2UserService;
import com.travel.community.pages.oauth.service.OAuth2SuccessHandler;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	@Value("${filter.exclude-address}")
	private String[] EXCLUDE_ADDRESS;

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2SuccessHandler oauth2SuccessHandler;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		return http
				.csrf(csrf -> csrf.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
						.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))

				.authorizeHttpRequests(auth -> auth
						.requestMatchers(EXCLUDE_ADDRESS)
						.permitAll()
						.anyRequest()
						.authenticated())
				// filter에서 exception 발생 처리
				.exceptionHandling(exception -> exception
						.authenticationEntryPoint(customAuthenticationEntryPoint))
				.addFilterBefore(
						jwtAuthenticationFilter,
						UsernamePasswordAuthenticationFilter.class)
				// .oauth2Login(Customizer.withDefaults())
				.oauth2Login(oauth2 -> oauth2
						.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
						.successHandler(oauth2SuccessHandler))

				.build();
	}
}

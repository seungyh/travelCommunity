package com.travel.community.pages.oauth.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.travel.community.global.exception.dto.BusinessException;
import com.travel.community.global.security.jwt.JwtManager;
import com.travel.community.pages.oauth.dto.CustomOAuth2User;
import com.travel.community.pages.oauth.enums.OAuthErrorCode;
import com.travel.community.pages.oauth.enums.ProviderType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${oauth.success-url}")
    private String SUCCESS_URL; // oauth 로그인 성공 시 보낼 url(base url부터 시작)

    private final JwtManager jwtManager;

    /**
     * oauth 로그인 성공 후 처리
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
        ProviderType type = null;
        switch (oauth2User.getProvider()) {
            case "Kakao":
                type = ProviderType.Kakao;
                break;

            default:
                throw new BusinessException(OAuthErrorCode.INVALID_OAUTH_PLATFORM);

        }

        String token = jwtManager.createJwt(oauth2User.getId().toString(), type);
        jwtManager.addCookie(response, token);

        response.sendRedirect(SUCCESS_URL);

    }

}

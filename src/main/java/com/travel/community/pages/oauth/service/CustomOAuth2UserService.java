package com.travel.community.pages.oauth.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.travel.community.pages.account.service.NickNameCreator;
import com.travel.community.pages.oauth.dto.CustomOAuth2User;
import com.travel.community.pages.oauth.entity.OAuthEntity;
import com.travel.community.pages.oauth.repository.OAuthRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuthRepository oAuthRepository;
    private final NickNameCreator nickNameCreator;

    /**
     * oauth 로그인 시 사용자 정보 조회 및 없으면 저장
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> properties = (Map<String, Object>) oauth2User.getAttributes().get("properties");

        String provider = userRequest.getClientRegistration().getClientName();
        String providerId = String.valueOf(oauth2User.getAttributes().get("id"));
        String nickname = nickNameCreator.getNickName();
        String profileImage = (String) properties.get("profile_image");

        OAuthEntity authEntity = oAuthRepository.findByProviderIdAndProvider(providerId, provider);

        // 첫 로그인이면 DB 저장
        if (authEntity == null) {
            authEntity = OAuthEntity.builder().providerId(providerId).provider(provider).nickName(nickname).build();
            oAuthRepository.save(authEntity);
        }

        return new CustomOAuth2User(authEntity.getId(), authEntity.getEmail(), nickname, profileImage, provider,
                properties, List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}

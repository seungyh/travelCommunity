package com.travel.community.pages.oauth.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.community.pages.account.entity.ProfileEntity;
import com.travel.community.pages.account.entity.UserEntity;
import com.travel.community.pages.account.enums.Roles;
import com.travel.community.pages.account.repository.AccountRepository;
import com.travel.community.pages.account.repository.ProfileRepository;
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
    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;

    /**
     * oauth 로그인 시 사용자 정보 조회 및 없으면 저장
     */
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> properties = (Map<String, Object>) oauth2User.getAttributes().get("properties");

        String provider = userRequest.getClientRegistration().getClientName();
        String providerId = String.valueOf(oauth2User.getAttributes().get("id"));
        String profileImage = (String) properties.get("profile_image");
        OAuthEntity authEntity = oAuthRepository.findByProviderIdAndProvider(providerId, provider);
        String nickName = nickNameCreator.getNickName();
        UserEntity userEntity = null;
        // 첫 로그인이면 DB 저장
        if (authEntity == null) {
            // users 테이블에 저장 후 userId를 id로 설정
            userEntity = UserEntity.builder().nickName(nickName).role(Roles.USER).build();
            accountRepository.save(userEntity);
            userEntity.setUserId(userEntity.getId());
            // oauth 정보 저장
            oAuthRepository.save(OAuthEntity.builder().providerId(providerId).provider(provider)
                    .userId(userEntity.getUserId()).build());
            // 프로필 이미지 저장
            profileRepository.save(ProfileEntity.create(null, profileImage, userEntity.getId()));
        } else {
            OAuthEntity oAuthEntity = oAuthRepository.findByProviderIdAndProvider(providerId, provider);
            ProfileEntity profileEntity = profileRepository.findByUserId(oAuthEntity.getUserId());
            profileEntity.setPath(profileImage);
        }

        return new CustomOAuth2User(authEntity.getId(), provider, properties,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}

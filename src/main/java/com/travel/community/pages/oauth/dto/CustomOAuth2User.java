package com.travel.community.pages.oauth.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.travel.community.pages.oauth.enums.ProviderType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private Long id; // db에 저장된 id
    private String email; // 사용자가 등록한 email
    private String provider; // 로그인한 플랫폼
    private String nickName; // 닉네임
    private String profileUrl; // 사용자 프로필 경로

    private final Map<String, Object> attributes;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomOAuth2User(Long id, String email, String nickName, String profileUrl, String provider,
            Map<String, Object> attributes, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.profileUrl = profileUrl;
        this.provider = provider;
        this.attributes = attributes;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

}

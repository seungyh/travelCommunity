package com.travel.community.pages.oauth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 로그인 플랫폼 구분
 * ProviderType
 */
@Getter
@RequiredArgsConstructor
public enum ProviderType {

    LOCAL("일반 login"),
    Kakao("카카오 login"),
    Google("google login"),
    ;

    private final String description;

}

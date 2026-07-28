package com.travel.community.global.security.jwt.dto;

import com.travel.community.pages.oauth.enums.ProviderType;

import lombok.Builder;
import lombok.Getter;

/**
 * 토큰 조회시 데이터 전달용 dto
 * TokenDto
 */
@Getter
@Builder
public class TokenDto {

    private String userId; // 사용자 ID
    private String token; // jwt 토큰
    private ProviderType type; // 로그인 플랫폼
}

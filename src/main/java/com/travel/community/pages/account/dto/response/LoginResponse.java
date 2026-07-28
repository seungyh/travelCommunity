package com.travel.community.pages.account.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 로그인 성공 사용자 정보 반환 객체
 * LoginResponse
 */
@Getter
@Builder
public class LoginResponse {
    private String userId; // 사용자 ID
    private String nickName; // 닉네임
    private String email; // 이메일
}

package com.travel.community.pages.oauth.exception;

import org.springframework.http.HttpStatus;

import com.travel.community.global.exception.CommonErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OAuthErrorCode implements CommonErrorCode {

    INVALID_OAUTH_PLATFORM(HttpStatus.BAD_REQUEST, "OAUTH-001", "지원하지 않는 로그인 플랫폼입니다.", "NOT SUPPORT LOGIN PLATFORM");

    private HttpStatus httpStatus;
    private String errorCode;
    private String errorMessage;
    private String logMessage;
}

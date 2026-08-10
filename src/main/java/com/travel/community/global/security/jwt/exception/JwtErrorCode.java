package com.travel.community.global.security.jwt.exception;

import org.springframework.http.HttpStatus;

import com.travel.community.global.exception.CommonErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtErrorCode implements CommonErrorCode {
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "JWT-001", "유효하지 않은 인증 정보입니다. 다시 로그인 해주세요.", "INVALID AUTH INFO"),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "JWT-002", "만료된 토큰입니다. 다시 로그인 해주세요.", "EXPIRED JWT TOKEN"),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;
    private final String logMessage;

}

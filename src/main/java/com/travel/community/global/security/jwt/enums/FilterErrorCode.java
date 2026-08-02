package com.travel.community.global.security.jwt.enums;

import org.springframework.http.HttpStatus;

import com.travel.community.global.exception.dto.CommonErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FilterErrorCode implements CommonErrorCode {
    INVALID_JWT(HttpStatus.BAD_REQUEST, "JWT-001", "유효하지 않은 인증 정보입니다. 다시 로그인 해주세요.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

}

package com.travel.community.global.exception.dto;

import org.springframework.http.HttpStatus;

/**
 * 에러 코드 인터페이스
 * 각 모듈별로 enum으로 구현하여 사용
 */
public interface CommonErrorCode {

    HttpStatus getHttpStatus();

    String getErrorCode();

    String getErrorMessage();
}

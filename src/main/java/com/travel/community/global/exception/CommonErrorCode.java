package com.travel.community.global.exception;

import org.springframework.http.HttpStatus;

/**
 * 에러 코드 인터페이스
 * 각 모듈별로 enum으로 구현하여 사용
 */
public interface CommonErrorCode {

    HttpStatus getHttpStatus(); // http status

    String getErrorCode(); // error code

    String getErrorMessage(); // 사용자에게 보일 메시지

    String getLogMessage(); // 로그 메시지
}

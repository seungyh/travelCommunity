package com.travel.community.pages.alarm.exception.enums;

import org.springframework.http.HttpStatus;

import com.travel.community.global.exception.CommonErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmErrorCode implements CommonErrorCode {

    NOT_EXIST_ALARM(HttpStatus.BAD_REQUEST, "BOARD-001", "존재하지 않는 알림 정보입니다."),
    INVALID_ACCESS(HttpStatus.BAD_REQUEST, "BOARD-002", "유효하지 않은 접근입니다."),
    ;

    private HttpStatus httpStatus;
    private String errorCode;
    private String errorMessage;
}

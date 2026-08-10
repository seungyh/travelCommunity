package com.travel.community.pages.account.exception.enums;

import org.springframework.http.HttpStatus;

import com.travel.community.global.exception.CommonErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountErrorCode implements CommonErrorCode {

    EXIST_ACCOUNT(HttpStatus.BAD_REQUEST, "ACCOUNT-001", "이미 존재하는 ID입니다.", "Already Exist User ID"),
    INVALID_ACCOUNT(HttpStatus.BAD_REQUEST, "ACCOUNT-002", "아이디 또는 비밀번호가 올바르지 않습니다.",
            "NOT EXIST USER_ID OR WRONG PASSWORD"),
            ;

    private HttpStatus httpStatus;
    private String errorCode;
    private String errorMessage;
    private String logMessage;
}

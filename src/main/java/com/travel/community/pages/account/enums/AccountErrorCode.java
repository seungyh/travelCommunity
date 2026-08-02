package com.travel.community.pages.account.enums;

import org.springframework.http.HttpStatus;

import com.travel.community.global.exception.dto.CommonErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountErrorCode implements CommonErrorCode {

    EXIST_ACCOUNT(HttpStatus.BAD_REQUEST, "ACCOUNT-001", "이미 존재하는 ID입니다.");

    private HttpStatus httpStatus;
    private String errorCode;
    private String errorMessage;
}

package com.travel.community.global.exception.dto;

import lombok.Getter;

/**
 * 비지니스 로직 커스텀 exception
 * BusinessException
 */
@Getter
public class BusinessException extends RuntimeException {

    private final CommonErrorCode errorCode;

    public BusinessException(CommonErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}

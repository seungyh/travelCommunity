package com.travel.community.pages.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private String errorCode; // 에러 코드
    private String errorMessage; // 에러 메시지

}

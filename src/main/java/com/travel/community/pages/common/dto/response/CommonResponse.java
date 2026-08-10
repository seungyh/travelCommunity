package com.travel.community.pages.common.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 성공/실패 응답 공통 DTO
 * CommonResponse
 */
@Builder
@Getter
public class CommonResponse {

    private boolean result;
    private String message;
}

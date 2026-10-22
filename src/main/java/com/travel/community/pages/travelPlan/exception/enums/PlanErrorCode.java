package com.travel.community.pages.travelPlan.exception.enums;

import org.springframework.http.HttpStatus;

import com.travel.community.global.exception.CommonErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlanErrorCode implements CommonErrorCode {

    NOT_EXIST_PLAN(HttpStatus.BAD_REQUEST, "PLAN-001", "존재하지 않는 여행 계획입니다."),
    INVALID_ACCESS(HttpStatus.BAD_REQUEST, "PLAN-002", "유효하지 않은 접근입니다."),
    NO_INVITE_PERMISSION(HttpStatus.BAD_REQUEST, "PLAN-003", "초대 권한이 없습니다."),
    ALREADY_MEMBER(HttpStatus.BAD_REQUEST, "PLAN-04", "이미 참여한 사용자입니다."),
    ALREADY_INVITED_MEMBER(HttpStatus.BAD_REQUEST, "PLAN-05", "이미 초대한 사용자입니다."),
    INVITE_SELF(HttpStatus.BAD_REQUEST, "PLAN-06", "자신은 초대할 수 없습니다."),
    NO_EXISTS_INVITE_INFO(HttpStatus.BAD_REQUEST, "PLAN-07", "초대 정보가 존재하지 않습니다."),
    ;

    private HttpStatus httpStatus;
    private String errorCode;
    private String errorMessage;
}

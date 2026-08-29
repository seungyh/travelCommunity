package com.travel.community.pages.board.exception.enums;

import org.springframework.http.HttpStatus;

import com.travel.community.global.exception.CommonErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardErrorCode implements CommonErrorCode {

    NOT_EXIST_BOARD(HttpStatus.BAD_REQUEST, "BOARD-001", "존재하지 않는 게시글입니다."),
    INVALID_ACCESS(HttpStatus.BAD_REQUEST, "BOARD-002", "유효하지 않은 접근입니다."),
    ;

    private HttpStatus httpStatus;
    private String errorCode;
    private String errorMessage;

}

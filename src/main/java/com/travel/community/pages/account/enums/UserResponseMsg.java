package com.travel.community.pages.account.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserResponseMsg {

    SIGN_UP_SUCCESS("회원가입을 성공하였습니다!"), //
    SIGN_UP_FAIL("회원가입을 실패하였습니다."),
    WRONG_LOGIN("아이디 또는 비밀번호가 올바르지 않습니다."),
    LOGIN_SUCCESS("로그인을 성공하였습니다."),
    LOGOUT_SUCCESS("로그아웃을 성공하였습니다."),
    // NO_EXIST_USER_ID("존재하지 않는 계정입니다."),
    // WRONG_PASSWORD("비밀번호가 ")
    ;

    private final String message;
}

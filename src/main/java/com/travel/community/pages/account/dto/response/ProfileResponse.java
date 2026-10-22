package com.travel.community.pages.account.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponse {
    private String bio; // 자기소개
    private String nickName; // 닉네임
    private String location; // 위치
}

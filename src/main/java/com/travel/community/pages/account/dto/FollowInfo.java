package com.travel.community.pages.account.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowInfo {
    private String userId; // userId
    private String nickName; // 닉네임
    private String bio; // 자기소개
    private String profilePath; // 프로필 이미지
}

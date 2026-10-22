package com.travel.community.pages.account.dto.request;

import com.travel.community.pages.account.enums.FollowTabType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 마이 페이지에서 팔로워 또는 팔로잉 탭 조회 요청 정보
 * FollowListRequest
 */
@Getter
@NoArgsConstructor
public class FollowListRequest {
    @NotBlank
    private String userId; // 조회 userId
    @NotNull
    private FollowTabType tabType; // 탭 타입
}

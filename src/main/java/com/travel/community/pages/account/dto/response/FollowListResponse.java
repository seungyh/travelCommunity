package com.travel.community.pages.account.dto.response;

import java.util.List;

import com.travel.community.pages.account.dto.FollowInfo;

import lombok.Builder;
import lombok.Getter;

/**
 * 마이페이지 팔로워 또는 팔로잉 목록 반환 정보
 * FollowListResponse
 */
@Getter
@Builder
public class FollowListResponse {

    private List<FollowInfo> followList;
    private int totalCount;
}

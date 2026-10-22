package com.travel.community.pages.account.dto.response;

import java.time.OffsetDateTime;

import lombok.Builder;
import lombok.Getter;

/**
 * 마이 페이지에서 사용자 정보 반환 정보
 */
@Getter
@Builder
public class MyPageResponse {

    private String userId; // userId
    private String nickName; // 닉네임
    private String email; // 이메일
    private OffsetDateTime createdAt; // 생성일
    private String bio; // 자기소개
    private String inviteCode; // 초대 코드
    private int boardCount; // 게시글 작성 개수
    private int allLikeCount; // 받은 좋아요 개수
    private int followingCount; // 팔로잉 수
    private int followerCount; // 팔로워 수
    private String profilePath; // 프로필 경로
    private String backgroundPath; // 배경 이미지 경로
    // private String location; // 위치
}

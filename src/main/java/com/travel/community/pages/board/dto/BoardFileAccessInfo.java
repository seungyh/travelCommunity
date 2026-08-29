package com.travel.community.pages.board.dto;

import com.travel.community.pages.board.enums.BoardStatus;
import com.travel.community.pages.board.enums.BoardVisibilityType;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 파일 이미지 반환 유효성 검사를 위한 정보
 * BoardFileAccessInfo
 */
@Getter
@NoArgsConstructor
public class BoardFileAccessInfo {

    private BoardStatus status; // 게시글 상태
    private BoardVisibilityType visibility; // 공개 범위
    private boolean following; // 해당 게시글 팔로우 여부
    private String userId; // 게시글의 user id
    private String path; // 파일 경로
}

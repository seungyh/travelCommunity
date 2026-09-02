package com.travel.community.pages.board.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 게시글 좋아요 반환 정보
 * BoardLikeResponse
 */
@Getter
@Builder
public class BoardLikeResponse {
    private int likeCount; // 게시글의 좋아요 개수
    private boolean liked; // 현재 게시글 좋아요 상태
}

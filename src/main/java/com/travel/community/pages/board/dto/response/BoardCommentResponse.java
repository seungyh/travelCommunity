package com.travel.community.pages.board.dto.response;

import java.util.List;

import com.travel.community.pages.board.dto.BoardCommentInfo;

import lombok.Builder;
import lombok.Getter;

/**
 * 댓글 조회 반환 정보
 * BoardCommentResponse
 */
@Getter
@Builder
public class BoardCommentResponse {
    private List<BoardCommentInfo> boardCommentList;
    private int totalCount;
}

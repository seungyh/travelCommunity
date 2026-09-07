package com.travel.community.pages.board.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글 등록 정보
 * BoardCommentRequest
 */
@Getter
@NoArgsConstructor
public class BoardCommentRequest {
    private Long boardId; // 게시글 id
    private Long parentId; // 상위 댓글 id
    private int depth; // 댑스
    private String content; // 댓글 내용
}

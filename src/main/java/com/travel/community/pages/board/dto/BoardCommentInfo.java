package com.travel.community.pages.board.dto;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 댓글 조회 정보
 * BoardCommentInfo
 */
@Getter
@Setter
public class BoardCommentInfo {
    private Long id; // 댓글 id
    private Long parentId; // 부모 댓글 id
    private String content; // 댓글 내용
    private OffsetDateTime createdAt; // 작성일
    private String userId; // 작성자 ID
    private String nickName; // 작성자 nickName
    private Long boardId; // 게시글 id
    private String profileImagePath; // 프로필 이미지 경로
    private Boolean isDel; // 삭제 여부

    public void setDeletedComment() {
        if (isDel) {
            this.content = "삭제된 댓글입니다.";
            this.userId = "";
            this.nickName = "";
            this.profileImagePath = "";
            this.createdAt = null;
        }
    }
}

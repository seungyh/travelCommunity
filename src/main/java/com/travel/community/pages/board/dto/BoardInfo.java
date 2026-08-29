package com.travel.community.pages.board.dto;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 게시글 조회 정보
 * BoardInfo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardInfo {

    private Long boardId; // 게시글 id
    private String userId; // 작성자 id
    private String nickName; // 작성자 nickName
    private boolean isFollow; // 나의 팔로우 여부
    private String title; // 제목
    private String content; // 내용
    private int likeCount; // 좋아요 개수
    private int sharedCount; // 공유된 횟수
    private int commentCount; // 댓글 수
    private OffsetDateTime createdAt; // 등록일
    private boolean isLike; // 좋아요 여부
    private String boardFileName; // 대표 이미지 이름
    private String profileName; // 프로필 이미지 이름

}

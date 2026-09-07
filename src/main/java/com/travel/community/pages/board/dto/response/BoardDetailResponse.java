package com.travel.community.pages.board.dto.response;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BoardDetailResponse {

    private Long boardId; // 게시글 ID
    private boolean follow; // 나의 팔로우 여부
    private String title; // 제목
    private String contentHtml; // 내용 HTML
    private int likeCount; // 좋아요 개수
    private int sharedCount; // 공유된 횟수
    private int commentCount; // 댓글 수
    private int viewCount; // 조회수
    private OffsetDateTime createdAt; // 등록일
    private boolean like; // 좋아요 여부
    private LocalDate travelStartAt; // 여행 시작일
    private LocalDate travelEndAt; // 여행 종료일
    private List<String> tags; // 해시태그
    private Long boardFeatureId; // 대표 이미지 ID
    private String profileImagePath; // 프로필 이미지 path
    private String userId; // 작성자 ID
    private String nickName; // 작성자 닉네임
    private String bio; // 자기소개
    private int boardCount; // 해당 작성자의 게시글 수

}

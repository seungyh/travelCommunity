package com.travel.community.pages.board.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.travel.community.pages.board.enums.BoardStatus;
import com.travel.community.pages.board.enums.BoardVisibilityType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardDraftResponse {

    private Long boardId; // 임시저장 후 게시글 ID
    private String title; // 제목
    private String content; // 내용
    private String contentHtml; // 내용 HTML
    private LocalDate travelStartAt; // 여행 시작일
    private LocalDate travelEndAt; // 여행 종료일
    private String category; // 여행 카테고리
    private List<String> tags; // 태그
    private String place; // 여행 장소
    private BoardVisibilityType visibility; // 공개 설정 (PUBLIC, FOLLOW, PRIVATE)
    private BoardStatus status; // 게시글 상태 (DRAFT, PUBLISHED)
    private Long featureImgId; // 대표 이미지 파일 ID
    private List<Long> extraImgIds; // 추가 이미지 파일 ID 목록

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setExtraImgIds(List<Long> extraImgIds) {
        this.extraImgIds = extraImgIds;
    }
}

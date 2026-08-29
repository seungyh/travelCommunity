package com.travel.community.pages.board.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.travel.community.pages.board.enums.BoardStatus;
import com.travel.community.pages.board.enums.BoardVisibilityType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 게시글 등록 요청 정보
 * BoardWriteRequest
 */
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class BoardWriteRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title; // 제목

    @Valid
    @NotNull(message = "유효하지 않은 내용입니다.")
    private ContentValue contentValue; // 내용

    @NotNull(message = "여행 시작일을 입력해주세요.")
    private LocalDate travelStartAt; // 여행 시작일

    @NotNull(message = "여행 마지막일을 입력해주세요.")
    private LocalDate travelEndAt; // 여행 마지막일

    @NotBlank(message = "카테고리를 선택해주세요.")
    private String category; // 카테고리

    private List<String> tags; // 태그

    @NotBlank(message = "여행 장소를 입력해주세요.")
    private String place; // 여행 장소

    @NotNull(message = "공개 범위를 선택해주세요.")
    private BoardVisibilityType visibility; // 전체 공개 ALL, 팔로워 공개 FOLLOW, 비공개 PRIVATE

    @NotNull(message = "상태는 NULL일 수 없습니다.")
    private BoardStatus status;

    @Data
    public static class ContentValue {
        @NotBlank(message = "내용을 입력해주세요.")
        private String content; // 게시글 내용 text만
        private String contentHtml; // 게시글 내용 html 전체

    }
}

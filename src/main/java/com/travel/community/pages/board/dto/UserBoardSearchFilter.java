package com.travel.community.pages.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/** 프로필 게시글 전용 검색 조건 */
@Getter
@NoArgsConstructor
public class UserBoardSearchFilter {
    private String category; // 전체는 null
    private String titleOrContent; // 제목 또는 내용
}
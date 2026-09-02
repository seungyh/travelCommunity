package com.travel.community.pages.board.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 조회 검색 조건
 * BoardSearchFilter
 */
@Getter
@NoArgsConstructor
public class BoardSearchFilter {

    private String titleOrContent; // 제목 또는 내용 검색 조건
    private String place; // 여행지
    private String nickName; // 작성자
    private List<String> tag; // 태그
    private String category; // 카테고리

    private Boolean isNext; // 다음인지 이전인지
    private LocalDateTime dateCursor; // 최신순일때 커서 기반 검색 하기 위한 보조 변수, 게시글의 등록일
    private int likeCursor; // 인기순일때 커서 기반 검색하기 위한 보조 변수, 게시글의 좋아요 개수
}

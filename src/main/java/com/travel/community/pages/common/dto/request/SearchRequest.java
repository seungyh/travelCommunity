package com.travel.community.pages.common.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 페이징 조회를 위한 공통 객체
 * SearchRequest
 * 
 * @param <T>
 */
@Getter
@NoArgsConstructor
public class SearchRequest<T> {
    private T filter; // 각 페이지별 검색 조건
    private String sortField; // 정렬 필드
    private SortDirection sortDirection; // 정렬 방향
    private int page; // 요청 페이지
    private int size; // 한 페이지별 개수

    public enum SortDirection {
        ASC, // 오름차순
        DESC, // 내림차순
        ;
    }
}

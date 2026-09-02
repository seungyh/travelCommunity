package com.travel.community.pages.board.dto.response;

import java.util.List;

import com.travel.community.pages.board.dto.BoardInfo;

import lombok.Builder;
import lombok.Getter;

/**
 * 게시글 목록 반환 정보
 * BoardSearchResponse
 */
@Getter
@Builder
public class BoardSearchResponse {

    private List<BoardInfo> boardSearchInfo; // 게시글 정보
    private int totalCount; // 게시글 총 개수

}

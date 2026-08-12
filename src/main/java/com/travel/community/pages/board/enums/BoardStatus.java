package com.travel.community.pages.board.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardStatus {
    PUBLISHED("게시"),
    DRAFT("임시 저장"),
    BLOCK("차단"),
    ;

    private String description;
}

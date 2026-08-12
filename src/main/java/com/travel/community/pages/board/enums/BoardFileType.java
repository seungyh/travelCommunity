package com.travel.community.pages.board.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardFileType {
    FEATURE("대표 이미지"),
    EXTRA("추가 이미지"),
    ;

    private String description;
}

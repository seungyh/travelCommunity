package com.travel.community.pages.board.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardVisibilityType {

    PUBLIC("전체 공개"),
    FOLLOW("팔로우 공개"),
    PRIVATE("비공개"),
    ;

    private String description;
}

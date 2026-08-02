package com.travel.community.pages.account.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Roles {
    USER("일반 사용자"), //
    MANAGER("관리자"), //
    ADMIN("총괄 관리자"), //
    ;

    private final String description;
}

package com.travel.community.global.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 여행 계획 접속자 정보
 * SocketUser
 */
@Getter
@AllArgsConstructor
public class SocketUser {
    private String userId;
    private String nickname;
    private String profilePath;
    private boolean syncProvider;
}

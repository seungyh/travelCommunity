package com.travel.community.pages.alarm.dto.response;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

/**
 * 여행 초대 알림 클릭 시 여행 계획 초대 정보 반환
 * PlanInviteInfoResponse
 */
@Getter
@Builder
public class PlanInviteInfoResponse {

    private String title; // 여행 제목
    private String place; // 장소
    private LocalDate startAt; // 시작일
    private LocalDate endAt; // 종료일
    private boolean alreadyAccept; // 이미 참여했는지 여부
}

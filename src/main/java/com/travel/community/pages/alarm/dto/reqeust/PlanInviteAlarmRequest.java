package com.travel.community.pages.alarm.dto.reqeust;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 여행 초대 알림 오픈 시 여행 계획 정보 요청 정보
 * PlanInviteAlarmRequest
 */
@Getter
@Setter
public class PlanInviteAlarmRequest {

    @NotBlank
    private String alarmId; // 알림 _id
    @NotNull
    private Long planId; // 계획 id
}

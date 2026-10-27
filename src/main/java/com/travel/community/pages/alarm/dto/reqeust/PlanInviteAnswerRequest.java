package com.travel.community.pages.alarm.dto.reqeust;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlanInviteAnswerRequest {

    @NotNull
    private Long planId; // 초대 여행 계획 id
    @NotNull
    private boolean answer; // 초대 or 거절
    @NotNull
    private String alarmId; // 알림 _id
}

package com.travel.community.pages.alarm.dto.response;

import java.util.List;

import com.travel.community.pages.alarm.dto.AlarmInfo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlarmListResponse {

    private List<AlarmInfo> alarmList; // 알람 목록
}

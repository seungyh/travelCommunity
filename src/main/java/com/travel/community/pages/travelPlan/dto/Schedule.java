package com.travel.community.pages.travelPlan.dto;

import com.travel.community.pages.travelPlan.enums.ScheduleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    private String id; // 스케줄 ID
    private int order; // 순서
    private ScheduleType type; // 스케줄 유형
    private String title; // 스케줄 제목
    private Place place; // 장소 정보
    private String memo; // 메모
    private Long cost; // 예상 비용
    private String startTime; // 시작 시간
    private String endTime; // 종료 시간
}

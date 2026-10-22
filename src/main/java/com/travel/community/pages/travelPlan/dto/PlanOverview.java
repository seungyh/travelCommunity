package com.travel.community.pages.travelPlan.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlanOverview {

    private Long planId; // 계획 id
    private String title; // 제목
    private LocalDate startAt; // 시작일
    private LocalDate endAt; // 종료일
    private String place; // 여행지
    private OffsetDateTime createdAt; // 생성일
}

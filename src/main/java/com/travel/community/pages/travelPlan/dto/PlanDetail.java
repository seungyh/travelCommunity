package com.travel.community.pages.travelPlan.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDetail {

    private String id; // 일차 ID
    private int order; // 순서
    private String title; // 일차 제목
    private String date; // 여행 날짜

    @Builder.Default
    private List<Schedule> schedules = new ArrayList<>();
}

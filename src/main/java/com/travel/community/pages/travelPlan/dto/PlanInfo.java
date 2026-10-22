package com.travel.community.pages.travelPlan.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.travel.community.pages.travelPlan.entity.postgre.TravelPlanEntity;
import com.travel.community.pages.travelPlan.enums.TravelPlanStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class PlanInfo {

    private Long id;
    private String title; // 여행 제목
    private LocalDate startAt; // 여행 시작일
    private LocalDate endAt; // 여행 종료일
    private String place; // 장소
    private OffsetDateTime createdAt; // 생성일
    private TravelPlanStatus status; // 상태 완료 - DONE, 계획중 - PLANNING
    private String userId;
    private int memberCount; // 멤버수

    public static PlanInfo from(TravelPlanEntity entity) {
        return PlanInfo.builder().id(entity.getId()).title(entity.getTitle()).startAt(entity.getStartAt())
                .endAt(entity.getEndAt()).place(entity.getPlace()).createdAt(entity.getCreatedAt())
                .status(entity.getStatus()).userId(entity.getUserId()).build();
    }
}

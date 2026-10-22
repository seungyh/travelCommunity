package com.travel.community.pages.travelPlan.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 여행 계획 생성 요청 정보
 * AddNewPlanRequest
 */
@Getter
@NoArgsConstructor
public class AddNewPlanRequest {

    @NotBlank
    private String title; // 제목

    @NotNull
    private LocalDate startAt; // 시작일

    @NotNull 
    private LocalDate endAt; // 종료일

    @NotBlank
    private String place; // 여행지
}

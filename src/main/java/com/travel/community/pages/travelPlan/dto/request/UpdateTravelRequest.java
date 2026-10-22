package com.travel.community.pages.travelPlan.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateTravelRequest {

    @NotNull
    private Long planId; // 여행 계획 정보 id
    @NotBlank
    private String title; // 제목
    @NotBlank
    private String place; // 장소
    @NotNull
    private LocalDate startAt; // 시작일
    @NotNull
    private LocalDate endAt; // 종료일
    private String memo; // 메모
}

package com.travel.community.pages.travelPlan.dto.response;

import java.util.List;

import com.travel.community.pages.travelPlan.dto.PlanOverview;

import lombok.Builder;
import lombok.Getter;

/**
 * 여행 계획 목록 반환 정보
 * PlanListResponse
 */
@Getter
@Builder
public class PlanListResponse {
    private List<PlanOverview> planList;
}

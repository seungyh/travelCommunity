package com.travel.community.pages.travelPlan.dto.request;

import java.util.List;

import com.travel.community.pages.travelPlan.dto.PlanDetail;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlanUpdateRequest {

    private List<PlanDetail> dayInfoList;
}

package com.travel.community.pages.travelPlan.dto.response;

import java.util.List;

import com.travel.community.pages.travelPlan.dto.PlanDetail;
import com.travel.community.pages.travelPlan.dto.PlanInfo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlanInfoResponse {

    private List<PlanDetail> planDetails;
    private PlanInfo planInfo;

}

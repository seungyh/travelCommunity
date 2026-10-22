package com.travel.community.pages.travelPlan.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.travel.community.pages.travelPlan.dto.PlanOverview;

@Mapper
public interface TravelPlanMapper {

    List<PlanOverview> getPlanList(String userId);

}

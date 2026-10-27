package com.travel.community.pages.travelPlan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.community.pages.common.dto.response.CommonResponse;
import com.travel.community.pages.travelPlan.dto.request.AddNewPlanRequest;
import com.travel.community.pages.travelPlan.dto.request.InviteMemberRequest;
import com.travel.community.pages.travelPlan.dto.request.PlanUpdateRequest;
import com.travel.community.pages.travelPlan.dto.request.UpdateTravelRequest;
import com.travel.community.pages.travelPlan.dto.response.PlanInfoResponse;
import com.travel.community.pages.travelPlan.dto.response.PlanListResponse;
import com.travel.community.pages.travelPlan.service.TravelPlanService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/web/api/travel")
@RequiredArgsConstructor
@RestController
@Slf4j
public class TravelPlanController {

    private final TravelPlanService travelPlanService;

    @GetMapping
    public ResponseEntity<PlanListResponse> getPlanList(HttpServletRequest request) {
        return ResponseEntity.ok(travelPlanService.getPlanList(request));
    }

    /**
     * 여행 계획 생성
     * 
     * @param request
     * @param addNewPlanRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<CommonResponse> addNewTravel(HttpServletRequest request,
            @Valid @RequestBody AddNewPlanRequest addNewPlanRequest) {
        return ResponseEntity.ok(travelPlanService.addNewTravel(request, addNewPlanRequest));
    }

    /**
     * 여행 정보 수정
     * 
     * @param request
     * @param updateTravelRequest
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateTravel(HttpServletRequest request,
            @Valid @RequestBody UpdateTravelRequest updateTravelRequest) {
        travelPlanService.updateTravel(request, updateTravelRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * 여행 계획 정보 조회
     * 
     * @param request
     * @param planId
     * @return
     */
    @GetMapping("/plan/{planId}")
    public ResponseEntity<PlanInfoResponse> getPlan(HttpServletRequest request, @PathVariable Long planId) {
        return ResponseEntity.ok(travelPlanService.getPlan(request, planId));
    }

    /**
     * 여행 계획 정보 수정
     * 
     * @param planId
     * @param updateRequest
     * @return
     */
    @PutMapping("/plan/{planId}")
    public ResponseEntity<Void> updatePlan(HttpServletRequest request, @PathVariable Long planId, @RequestBody PlanUpdateRequest updateRequest) {
        travelPlanService.updatePlan(request, planId, updateRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * 여행 계획에 멤버 초대
     * 
     * @param request
     * @param inviteMemberRequest
     * @return
     */
    @PostMapping("/plan/invite")
    public ResponseEntity<Void> inviteMember(HttpServletRequest request,
            @RequestBody InviteMemberRequest inviteMemberRequest) {
        travelPlanService.inviteMember(request, inviteMemberRequest);
        return ResponseEntity.noContent().build();
    }
}

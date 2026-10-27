package com.travel.community.pages.travelPlan.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.travel.community.global.exception.BusinessException;
import com.travel.community.global.security.jwt.JwtManager;
import com.travel.community.pages.account.entity.UserEntity;
import com.travel.community.pages.account.exception.enums.AccountErrorCode;
import com.travel.community.pages.account.repository.AccountRepository;
import com.travel.community.pages.alarm.enmus.AlarmType;
import com.travel.community.pages.alarm.service.AlarmService;
import com.travel.community.pages.common.dto.response.CommonResponse;
import com.travel.community.pages.travelPlan.dto.PlanInfo;
import com.travel.community.pages.travelPlan.dto.PlanOverview;
import com.travel.community.pages.travelPlan.dto.request.AddNewPlanRequest;
import com.travel.community.pages.travelPlan.dto.request.InviteMemberRequest;
import com.travel.community.pages.travelPlan.dto.request.PlanUpdateRequest;
import com.travel.community.pages.travelPlan.dto.request.UpdateTravelRequest;
import com.travel.community.pages.travelPlan.dto.response.PlanInfoResponse;
import com.travel.community.pages.travelPlan.dto.response.PlanListResponse;
import com.travel.community.pages.travelPlan.entity.mongo.TravelPlanDocument;
import com.travel.community.pages.travelPlan.entity.postgre.TravelPlanEntity;
import com.travel.community.pages.travelPlan.entity.postgre.TravelPlanInvitationEntity;
import com.travel.community.pages.travelPlan.enums.TravelPlanStatus;
import com.travel.community.pages.travelPlan.exception.enums.PlanErrorCode;
import com.travel.community.pages.travelPlan.repository.mapper.TravelPlanMapper;
import com.travel.community.pages.travelPlan.repository.mongo.TravelPlanDetailRepository;
import com.travel.community.pages.travelPlan.repository.postgre.TravelPlanInvitationRepository;
import com.travel.community.pages.travelPlan.repository.postgre.TravelPlanMemberRepository;
import com.travel.community.pages.travelPlan.repository.postgre.TravelPlanRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelPlanService {

    private final TravelPlanRepository travelPlanRepository;
    private final TravelPlanDetailRepository travelPlanDetailRepository;
    private final TravelPlanMemberRepository travelPlanMemberRepository;
    private final TravelPlanInvitationRepository travelPlanInvitationRepository;
    private final AccountRepository accountRepository;
    private final TravelPlanMapper travelPlanMapper;
    private final AlarmService alarmService;
    private final JwtManager jwtManager;

    public CommonResponse addNewTravel(HttpServletRequest request, AddNewPlanRequest addNewPlanRequest) {

        String userId = jwtManager.getUserId(request);

        TravelPlanEntity travelPlanEntity = TravelPlanEntity.builder().title(addNewPlanRequest.getTitle())
                .startAt(addNewPlanRequest.getStartAt())
                .status(TravelPlanStatus.PLANNING)
                .endAt(addNewPlanRequest.getEndAt()).userId(userId).place(addNewPlanRequest.getPlace()).build();

        travelPlanRepository.save(travelPlanEntity);
        travelPlanDetailRepository.save(TravelPlanDocument.builder().id(travelPlanEntity.getId()).build());

        return CommonResponse.builder().result(true).message(travelPlanEntity.getId().toString()).build();
    }

    public PlanInfoResponse getPlan(HttpServletRequest request, Long planId) {
        // 여행 개요 정보 조회
        TravelPlanEntity planEntity = travelPlanRepository.findById(planId)
                .orElseThrow(() -> new BusinessException(PlanErrorCode.NOT_EXIST_PLAN));
        checkPlanAccess(request, planEntity);
        PlanInfo planInfo = PlanInfo.from(planEntity);
        // 해당 계획에 멤버수 조회, 방 생성자는 다른 테이블에 존재하므로 +1
        int memberCount = travelPlanMemberRepository.countByPlanId(planId) + 1;

        // 여행 계획 조회
        TravelPlanDocument planDocument = travelPlanDetailRepository.findById(planId).orElse(new TravelPlanDocument());

        return PlanInfoResponse.builder().planInfo(planInfo.toBuilder().memberCount(memberCount).build())
                .planDetails(planDocument.getPlans())
                .build();

    }

    @Transactional
    public void updatePlan(HttpServletRequest request, Long planId, PlanUpdateRequest updateRequest) {
        TravelPlanEntity plan = travelPlanRepository.findById(planId)
                .orElseThrow(() -> new BusinessException(PlanErrorCode.NOT_EXIST_PLAN));
        checkPlanAccess(request, plan);
        TravelPlanDocument planDocument = travelPlanDetailRepository.findById(planId).orElse(new TravelPlanDocument());
        planDocument.updatePlans(updateRequest.getDayInfoList());
        travelPlanDetailRepository.save(planDocument);
    }

    private void checkPlanAccess(HttpServletRequest request, TravelPlanEntity plan) {
        String userId = jwtManager.getUserId(request);
        if (!plan.getUserId().equals(userId)
                && !travelPlanMemberRepository.existsByPlanIdAndUserId(plan.getId(), userId)) {
            throw new BusinessException(PlanErrorCode.INVALID_ACCESS);
        }
    }

    public void inviteMember(HttpServletRequest request, InviteMemberRequest inviteMemberRequest) {
        String userId = jwtManager.getUserId(request);

        TravelPlanEntity travelPlanEntity = travelPlanRepository.findById(inviteMemberRequest.getPlanId())
                .orElseThrow(() -> new BusinessException(PlanErrorCode.NOT_EXIST_PLAN));
        // 생성자인지 체크
        if (!userId.equals(travelPlanEntity.getUserId())) {
            throw new BusinessException(PlanErrorCode.NO_INVITE_PERMISSION);
        }

        // 초대할 사용자 코드로 조회
        UserEntity userEntity = accountRepository.getByInviteCode(inviteMemberRequest.getTag())
                .orElseThrow(() -> new BusinessException(AccountErrorCode.NOT_EXIST_ACCOUNT));

        // 자신을 초대하는지 체크
        if (userEntity.getUserId().equals(userId)) {
            throw new BusinessException(PlanErrorCode.INVITE_SELF);
        }
        // 이미 참여하고 있는 사용자인지 체크
        if (travelPlanMemberRepository.existsByPlanIdAndUserId(inviteMemberRequest.getPlanId(),
                userEntity.getUserId())) {
            throw new BusinessException(PlanErrorCode.ALREADY_MEMBER);
        }

        // 이미 초대한 사용자인지 체크
        if (travelPlanInvitationRepository.existsByInviteeIdAndPlanId(userEntity.getUserId(),
                inviteMemberRequest.getPlanId())) {
            throw new BusinessException(PlanErrorCode.ALREADY_INVITED_MEMBER);
        }

        // 초대 테이블에 저장
        travelPlanInvitationRepository.save(TravelPlanInvitationEntity.builder().inviterId(userId)
                .inviteeId(userEntity.getUserId()).planId(inviteMemberRequest.getPlanId()).build());

        // 알림 저장
        alarmService.saveAlarm(userId, userEntity.getUserId(), travelPlanEntity.getTitle(), AlarmType.PLAN,
                travelPlanEntity.getId());
    }

    @Transactional
    public void updateTravel(HttpServletRequest request, UpdateTravelRequest updateTravelRequest) {
        String userId = jwtManager.getUserId(request);

        TravelPlanEntity planEntity = travelPlanRepository.findByUserIdAndId(userId, updateTravelRequest.getPlanId())
                .orElseThrow(() -> new BusinessException(PlanErrorCode.NOT_EXIST_PLAN));
        planEntity.updateTravelInfo(updateTravelRequest.getTitle(), updateTravelRequest.getStartAt(),
                updateTravelRequest.getEndAt(), updateTravelRequest.getPlace(), updateTravelRequest.getMemo());
    }

    public PlanListResponse getPlanList(HttpServletRequest request) {
        String userId = jwtManager.getUserId(request);
        List<PlanOverview> planList = travelPlanMapper.getPlanList(userId);

        return PlanListResponse.builder().planList(planList).build();
    }
}

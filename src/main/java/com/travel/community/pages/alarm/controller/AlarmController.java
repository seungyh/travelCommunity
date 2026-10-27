package com.travel.community.pages.alarm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.community.pages.alarm.dto.reqeust.PlanInviteAlarmRequest;
import com.travel.community.pages.alarm.dto.reqeust.PlanInviteAnswerRequest;
import com.travel.community.pages.alarm.dto.response.AlarmListResponse;
import com.travel.community.pages.alarm.dto.response.PlanInviteInfoResponse;
import com.travel.community.pages.alarm.service.AlarmService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/web/api/alarm")
public class AlarmController {

    private final AlarmService alarmService;

    @PostMapping("/read/{alarmId}")
    public ResponseEntity<Integer> readAlarm(HttpServletRequest request, @PathVariable String alarmId) {
        return ResponseEntity.ok(alarmService.readAlarm(request, alarmId));
    }

    /**
     * 안읽은 알림 목록 조회
     * 
     * @param request
     * @return
     */
    @GetMapping
    public ResponseEntity<AlarmListResponse> getAlarmList(HttpServletRequest request) {
        return ResponseEntity.ok(alarmService.getAlarmList(request));
    }

    /**
     * 안읽은 알림 카운트 조회
     * 
     * @param request
     * @return
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> getAlarmCount(HttpServletRequest request) {
        return ResponseEntity.ok(alarmService.getAlarmCount(request));
    }

    /**
     * 여행 계획 초대 알림 정보 반환
     * 
     * @param request
     * @param planId
     * @return
     */
    @GetMapping("/plan/invite")
    public ResponseEntity<PlanInviteInfoResponse> getInviteInfo(HttpServletRequest request,
            @Valid @ModelAttribute PlanInviteAlarmRequest planInviteAlarmRequest) {
        return ResponseEntity.ok(alarmService.getInviteInfo(request, planInviteAlarmRequest));
    }

    /**
     * 알림에서 여행 계획 초대 수락 또는 거절
     * 
     * @param request
     * @param answerInfo
     * @return
     */
    @PostMapping("/plan/invite")
    public ResponseEntity<Void> answerPlanInvite(HttpServletRequest request,
            @Valid @RequestBody PlanInviteAnswerRequest answerInfo) {
        alarmService.answerPlanInvite(request, answerInfo);
        return ResponseEntity.noContent().build();
    }

}

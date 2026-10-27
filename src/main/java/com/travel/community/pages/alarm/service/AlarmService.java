package com.travel.community.pages.alarm.service;

import java.time.Instant;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.travel.community.global.exception.BusinessException;
import com.travel.community.global.security.jwt.JwtManager;
import com.travel.community.pages.account.entity.UserEntity;
import com.travel.community.pages.account.exception.enums.AccountErrorCode;
import com.travel.community.pages.account.repository.AccountRepository;
import com.travel.community.pages.alarm.dto.AlarmInfo;
import com.travel.community.pages.alarm.dto.reqeust.PlanInviteAlarmRequest;
import com.travel.community.pages.alarm.dto.reqeust.PlanInviteAnswerRequest;
import com.travel.community.pages.alarm.dto.response.AlarmListResponse;
import com.travel.community.pages.alarm.dto.response.PlanInviteInfoResponse;
import com.travel.community.pages.alarm.enmus.AlarmType;
import com.travel.community.pages.alarm.entity.AlarmDocument;
import com.travel.community.pages.alarm.exception.enums.AlarmErrorCode;
import com.travel.community.pages.alarm.repository.mongo.AlarmRepository;
import com.travel.community.pages.travelPlan.entity.postgre.TravelPlanEntity;
import com.travel.community.pages.travelPlan.entity.postgre.TravelPlanMemberEntity;
import com.travel.community.pages.travelPlan.exception.enums.PlanErrorCode;
import com.travel.community.pages.travelPlan.repository.postgre.TravelPlanInvitationRepository;
import com.travel.community.pages.travelPlan.repository.postgre.TravelPlanMemberRepository;
import com.travel.community.pages.travelPlan.repository.postgre.TravelPlanRepository;
import com.travel.community.sse.sseManager.SseEmitterManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlarmService {

	private final AccountRepository accountRepository;
	private final AlarmRepository alarmRepository;
	private final SseEmitterManager sseEmitterManager;
	private final TravelPlanInvitationRepository travelPlanInvitationRepository;
	private final TravelPlanRepository travelPlanRepository;
	private final TravelPlanMemberRepository travelPlanMemberRepository;
	private final JwtManager jwtManager;

	private final MongoTemplate mongoTemplate;

	// 로그인한 사용자가 받은 알림만 읽음 처리
	public Integer readAlarm(HttpServletRequest request, String alarmId) {
		String userId = jwtManager.getUserId(request);
		if (!ObjectId.isValid(alarmId)) {
			throw new BusinessException(AlarmErrorCode.NOT_EXIST_ALARM);
		}
		Query query = new Query(Criteria.where("_id").is(new ObjectId(alarmId))
				.and("receiverId").is(userId));
		long matchedCount = mongoTemplate.updateFirst(query, new Update().set("read", true),
				AlarmDocument.class).getMatchedCount();
		if (matchedCount == 0) {
			throw new BusinessException(AlarmErrorCode.NOT_EXIST_ALARM);
		}
		return alarmRepository.countByReceiverIdAndReadFalse(userId);
	}

	public AlarmListResponse getAlarmList(HttpServletRequest request) {
		String userId = jwtManager.getUserId(request);
		// 안읽은 알람 5개만 조회
		List<AlarmDocument> alarmList = alarmRepository
				.findTop5ByReceiverIdAndReadFalseOrderByCreatedAtDesc(userId);

		return AlarmListResponse.builder().alarmList(AlarmInfo.from(alarmList)).build();

	}

	// 안읽은 알람 개수 조회
	public Integer getAlarmCount(HttpServletRequest request) {
		String userId = jwtManager.getUserId(request);
		return alarmRepository.countByReceiverIdAndReadFalse(userId);

	}

	// 알림 저장
	public void saveAlarm(String userId, String receiverId, String description, AlarmType alarmType, Long planId) {
		UserEntity user = accountRepository.findByUserId(userId)
				.orElseThrow(() -> new BusinessException(AccountErrorCode.NOT_EXIST_ACCOUNT));
		AlarmDocument doc = alarmRepository.save(AlarmDocument.builder().alarmType(alarmType)
				.createdAt(Instant.now())
				.description(description).receiverId(receiverId).senderId(userId)
				.senderNickName(user.getNickName())
				.planId(planId)
				.build());

		sendAlarm(receiverId, AlarmInfo.from(doc));

	}

	// 알림 실시간 전송
	private void sendAlarm(String userId, AlarmInfo info) {
		sseEmitterManager.send(userId, info);

	}

	public PlanInviteInfoResponse getInviteInfo(HttpServletRequest request,
			PlanInviteAlarmRequest planInviteAlarmRequest) {
		String userId = jwtManager.getUserId(request);
		boolean alreadyAccept = false;

		// 해당 여행 계획으로 초대된 적이 있는지 체크
		boolean wasInvited = travelPlanInvitationRepository.existsByInviteeIdAndPlanId(userId,
				planInviteAlarmRequest.getPlanId());
		if (!wasInvited) {
			// 초대 정보가 없으면 이미 참여한 사용자인지 체크
			alreadyAccept = travelPlanMemberRepository
					.existsByUserIdAndPlanId(userId, planInviteAlarmRequest.getPlanId());

			// 이미 참여한 사용자도 아니면 에러
			if (!alreadyAccept) {
				throw new BusinessException(PlanErrorCode.NO_EXISTS_INVITE_INFO);
			}
		}

		TravelPlanEntity planEntity = travelPlanRepository.findById(planInviteAlarmRequest.getPlanId())
				.orElseThrow(() -> new BusinessException(PlanErrorCode.NOT_EXIST_PLAN));

		return PlanInviteInfoResponse.builder().endAt(planEntity.getEndAt()).startAt(planEntity.getStartAt())
				.title(planEntity.getTitle()).place(planEntity.getPlace()).alreadyAccept(alreadyAccept).build();
	}

	@Transactional
	public void answerPlanInvite(HttpServletRequest request, PlanInviteAnswerRequest answerInfo) {

		String userId = jwtManager.getUserId(request);

		// 해당 여행 계획으로 초대된 적이 있는지 체크
		travelPlanInvitationRepository.findByInviteeIdAndPlanId(userId, answerInfo.getPlanId())
				.orElseThrow(() -> new BusinessException(PlanErrorCode.NO_EXISTS_INVITE_INFO));

		if (answerInfo.isAnswer()) {
			// 초대 수락
			travelPlanMemberRepository
					.save(TravelPlanMemberEntity.builder().planId(answerInfo.getPlanId())
							.userId(userId).build());
		}
		travelPlanInvitationRepository.deleteByInviteeIdAndPlanId(userId, answerInfo.getPlanId()); // 초대 정보 삭제

		// 알림 읽음 처리
		readAlarm(new ObjectId(answerInfo.getAlarmId()));
	}

	// 알림 읽음 처리
	public void readAlarm(ObjectId alarmId) {
		Query query = new Query(
				Criteria.where("_id").is(alarmId));

		Update update = new Update()
				.set("read", true);

		mongoTemplate.updateFirst(
				query,
				update,
				AlarmDocument.class);
	}

}

export interface PlanInviteAnswerRequest {
	planId: number; // 계획 id
	answer: boolean; // 수락이면 true, 거절이면 false
	alarmId: string; // 알림 _id
}

import type { DayInfo, PlanInfo } from "../PlanInfo";

export interface PlanInfoResponse {
	planDetails: DayInfo[]; // 계획 스케줄
	planInfo: PlanInfo; // 계획 정보
}

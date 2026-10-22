import type { DayInfo } from "../PlanInfo";

/**
 * 계획 변경 요청 정보
 */
export interface PlanInfoUpdateRequest {
	dayInfoList: DayInfo[];
}

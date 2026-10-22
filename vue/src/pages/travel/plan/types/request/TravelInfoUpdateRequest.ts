import type { Place } from "../PlanInfo";

export interface TravelInfoUpdateRequest {
	planId: number; // 여행 계획 정보 id
	title: string; // 제목
	place: string; // 장소
	startAt: string; // 시작일
	endAt: string; // 종료일
	memo: string; // 메모
}

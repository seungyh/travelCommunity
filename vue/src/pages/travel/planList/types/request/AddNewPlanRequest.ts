/**
 * 새 여행 계획 생성 요청 정보
 */
export interface AddNewPlanRequest {
	title: string; // 제목
	startAt: Date | null; // 시작일
	endAt: Date | null; // 종료일
	place: string; // 여행지
}

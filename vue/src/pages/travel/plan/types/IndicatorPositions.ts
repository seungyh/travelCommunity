/**
 * 다른 사용자의 수정 조회 위치 정보
 */
export interface IndicatorPositions {
	userId: string; // userId
	nickName: string; // nickName
	planId: string | null; //planId
	scheduleId: string | null; // scheduleId
	top: number; // top px 정보
	left: number; // left px 정보
	visible: boolean; // 보여짐 여부
	isEditing: boolean; // 수정인지 조회인지 여부
}

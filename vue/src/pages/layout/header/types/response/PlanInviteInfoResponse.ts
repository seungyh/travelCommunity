export interface PlainInviteInfoResponse {
	title: string; // 여행 제목
	place: string; // 장소
	startAt: Date | null; // 시작일
	endAt: Date | null; // 종료일
	alreadyAccept: boolean; // 이미 참여했는지 여부
}

export interface EditingUser {
	userId: string; // 수정중인 user id
	nickName: string; // 수정중인 user nickName
	planId: string | null; // 수정중인 일정 id
	scheduleId: string | null; // 수정중인 스케줄 id
	isEditing: boolean; // 수정 여부(false면 조회중)
}

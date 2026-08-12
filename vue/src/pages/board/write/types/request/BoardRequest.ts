// 게시글 등록/수정 요청 객체
export interface BoardRequest {
	title: string; // 제목
	content: string; // 내용
	travelStartAt: Date | null; // 여행 시작일
	travelEndAt: Date | null; // 여행 종료일
	category: string; // 여행 카테고리
	tags: string[]; // 태그
	place: string; // 여행 장소
	visibility: string; // 공개 설정(전체, 팔로우 공개, 비공개)
	status: string; // 상태(DRAFT: 임시 저장, PUBLIC: 게시)
}

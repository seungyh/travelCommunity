/**
 * 게시글 상세보기 정보
 */
export interface BoardDetailResponse {
	boardId: number; // 게시글 id
	follow: boolean; // 나의 팔로우 여부
	title: string; // 제목
	contentHtml: string; // 내용
	likeCount: number; // 좋아요 개수
	sharedCount: number; // 공유된 횟수
	commentCount: number; // 댓글 수
	viewCount: number; // 조회수
	createdAt: Date; // 등록일
	like: boolean; // 좋아요 여부
	travelStartAt: Date; // 여행 시작일
	travelEndAt: Date; // 여행 종료일
	tags: string[]; // 해시태그
	boardFeatureId: number | string; // 대표 이미지 id
	profileImagePath: string; // 프로필 이미지 path
	userId: string; // 작성자 id
	nickName: string; // 작성자 nickName
	bio: string; // 작성자 간단 자기소개
	boardCount: number; // 해당 작성자의 게시글 수
}

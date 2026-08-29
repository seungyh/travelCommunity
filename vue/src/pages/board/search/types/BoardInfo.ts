export interface BoardInfo {
	boardId: number; // 게시글 id
	userId: string; // 작성자 id
	nickName: string; // 작성자 nickName
	follow: boolean; // 나의 팔로우 여부
	title: string; // 제목
	content: string; // 내용
	likeCount: number; // 좋아요 개수
	sharedCount: number; // 공유된 횟수
	commentCount: number; // 댓글 수
	createdAt: Date; // 등록일
	like: boolean; // 좋아요 여부
	boardFileName: string; // 대표 이미지 이름
	profileName: string; // 프로필 이미지 이름
}

/**
 * 댓글 정보
 */
export interface BoardCommentInfo {
	id: number; // 댓글 id
	parentId: number; // 부모 댓글 id
	content: string; // 댓글 내용
	createdAt: Date; // 작성일
	userId: string; // 작성자 ID
	nickName: string; // 작성자 nickName
	boardId: number; // 게시글 id
	isDel: boolean; // 삭제 여부
	profileImagePath: string; // 프로필 이미지 path
}

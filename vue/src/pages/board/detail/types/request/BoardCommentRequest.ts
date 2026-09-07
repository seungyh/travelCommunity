/**
 * 댓글 작성 정보
 */
export interface BoardCommentRequest {
	boardId: number; // 게시글 id
	parentId: number; // 상위 댓글 id
	content: string; // 댓글 내용
	depth: number; // 댑스
}

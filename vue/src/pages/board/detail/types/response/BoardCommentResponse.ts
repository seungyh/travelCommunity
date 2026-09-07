import type { BoardCommentInfo } from "../BoardCommentInfo";

export interface BoardCommentResponse {
	boardCommentList: BoardCommentInfo[]; // 게시글의 전체 댓글
	totalCount: number;
}

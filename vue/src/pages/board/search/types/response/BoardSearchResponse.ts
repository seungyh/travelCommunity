import type { BoardSearchInfo } from "../BoardSearchInfo";

/**
 * 게시글 목록 조회 정보
 */
export interface BoardSearchResponse {
	boardSearchInfo: BoardSearchInfo[];
	totalCount: number;
}

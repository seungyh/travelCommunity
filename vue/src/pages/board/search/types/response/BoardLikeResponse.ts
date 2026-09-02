/**
 * 게시글 좋아요 반환 정보
 */
export interface BoardLikeResponse {
	likeCount: number; // 게시글 좋아요 개수
	liked: boolean; // 좋아요 상태
}

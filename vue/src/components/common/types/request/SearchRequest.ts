/**
 * 서버로 요청 보낼 페이지 조회 공통 객체
 */
export interface SearchRequest<T> {
	filter: T; // 페이지별 검색 조건
	sortField: string; // 정렬 필드
	sortDirection: "ASC" | "DESC"; // 정렬 방향
	page: number; // 요청 페이지
	size: number; // 한 화면에 표현 최대 개수
}

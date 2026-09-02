export interface BoardSearchFilter {
	titleOrContent: string | null; // 제목 또는 내용 검색 조건
	place: string | null; // 여행지
	nickName: string | null; // 작성자
	tag: string[]; // 태그
	category: string | null; // 카테고리

	isNext: boolean; // 스크롤 밑으로 내렸는지 위로 올렸는지
	dateCursor: Date; // 최신순일때 커서 기반 검색 하기 위한 보조 변수, 게시글의 등록일
	likeCursor: number; // 인기순일때 커서
}

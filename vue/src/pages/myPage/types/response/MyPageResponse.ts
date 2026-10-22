/**
 * 마이페이지 정보
 */
export interface MyPageResponse {
	userId: string; // user id
	nickName: string; // 닉네임
	email: string; // 이메일
	createdAt: string; // 생성일
	bio: string; // 자기소개
	inviteCode: string; // 초대 코드
	boardCount: number; // 게시글 작성 개수
	allLikeCount: number; // 받은 좋아요 개수
	followingCount: number; // 팔로잉 수
	followerCount: number; // 팔로워 수
	profilePath: string; // 프로필 경로
	backgroundPath: string; // 배경 경로
}

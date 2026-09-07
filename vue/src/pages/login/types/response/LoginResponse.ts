/**
 * 로그인 성공시 사용자 정보 반환 객체
 */
export interface LoginResponse {
	userId: string; // 사용자 ID
	nickName: string; // 사용자 닉네임
	email: string; // 사용자 이메일
	profileImagePath: string; // 프로필 이미지 경로
}

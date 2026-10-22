/**
 * web socket에 연결중인 사용자 정보
 */
export interface ConnectedUser {
	userId: string; // user id
	nickName: string; // 닉네임
	profilePath: string; // 프로필 path
	syncProvider: boolean; // 신규 참가자에게 초기 Y.Doc 상태를 제공하는 사용자 여부
}

import type { FollowInfo } from "../FollowInfo";

export interface FollowListResponse {
	followList: FollowInfo[]; // 팔로워 또는 팔로잉 목록
	totalCount: number;
}

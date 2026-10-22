import type { TabValue } from "../../enmus/TabValue";

export interface FollowListRequest {
	userId: string;
	tabType: TabValue; // 팔로워 탭 or 팔로잉 탭
}

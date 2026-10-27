import type { AlarmType } from "../enmus/AlarmType";

export interface AlarmInfo {
	id: string; // object id
	nickName: string; // 닉네임
	description: string; // 채팅 또는 계획 제목
	createdAt: Date; //  생성일
	alarmType: AlarmType; // 알람 타입
	senderId: string; // 보낸 사람 userId
	read: boolean; // 읽음 여부

	planId: number; // 계획 id - alarmType이 PLAN일 때만
}

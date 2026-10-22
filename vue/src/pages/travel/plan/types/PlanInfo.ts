import type { IconProp } from "@fortawesome/fontawesome-svg-core";

export enum ScheduleType {
	MOVE = "MOVE",
	FOOD = "FOOD",
	TOUR = "TOUR",
	HOTEL = "HOTEL",
	ACTIVITY = "ACTIVITY",
	SHOPPING = "SHOPPING",
	REST = "REST",
	OTHER = "OTHER",
}

export interface Place {
	name: string;
	address: string;
	latitude: number | null;
	longitude: number | null;
	placeId: string;
}

export interface Schedule {
	id: string;
	order: number; // 스케줄 순서
	type: ScheduleType | null; // 스케줄 타입
	title: string; // 제목
	place: Place; // 장소
	memo: string; // 메모
	cost: number; // 비용
	startTime: string; // 시작 시간
	endTime: string; // 종료 시간
}

export interface DayInfo {
	id: string;
	order: number; // 일정 순서
	title: string; // 제목
	date: string | null; // 날짜
	schedules: Schedule[];
}

export interface PlanInfo {
	id: number;
	title: string;
	place: string;
	startAt: string;
	endAt: string;
	memberCount: number; // 멤버수
}

export interface ScheduleCategoryItem {
	label: string;
	icon: IconProp;
	iconClass: string;
	chipClass: string;
	activeClass: string;
}

// 스케줄에서 일반 input/textarea로 수정 가능한 필드
export type ScheduleTextField = "title" | "memo" | "address";

// 스케줄에서 시간 선택으로 수정 가능한 필드
export type ScheduleTimeField = "startTime" | "endTime";

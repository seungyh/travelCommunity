import {
	ScheduleType,
	type ScheduleCategoryItem,
} from "../types/PlanInfo";

export const scheduleCategoryMap: Record<ScheduleType, ScheduleCategoryItem> = {
	[ScheduleType.MOVE]: {
		label: "이동",
		icon: ["fas", "car"],
		iconClass: "text-sky-600",
		chipClass: "border-sky-200 bg-sky-50 text-sky-600",
		activeClass: "text-sky-500 text-sm font-semibold",
	},
	[ScheduleType.FOOD]: {
		label: "식사",
		icon: ["fas", "utensils"],
		iconClass: "text-orange-600",
		chipClass: "border-orange-200 bg-orange-50 text-orange-600",
		activeClass: "text-orange-500 text-sm font-semibold",
	},
	[ScheduleType.TOUR]: {
		label: "관광",
		icon: ["fas", "camera"],
		iconClass: "text-teal-600",
		chipClass: "border-teal-200 bg-teal-50 text-teal-600",
		activeClass: "text-sm font-semibold text-teal-500",
	},
	[ScheduleType.HOTEL]: {
		label: "숙소",
		icon: ["fas", "hotel"],
		iconClass: "text-indigo-600",
		chipClass: "border-indigo-200 bg-indigo-50 text-indigo-600",
		activeClass: "text-sm font-semibold text-indigo-500",
	},
	[ScheduleType.ACTIVITY]: {
		label: "액티비티",
		icon: ["fas", "person-running"],
		iconClass: "text-rose-600",
		chipClass: "border-rose-200 bg-rose-50 text-rose-600",
		activeClass: "text-sm font-semibold text-rose-500",
	},
	[ScheduleType.SHOPPING]: {
		label: "쇼핑",
		icon: ["fas", "bag-shopping"],
		iconClass: "text-amber-600",
		chipClass: "border-amber-200 bg-amber-50 text-amber-600",
		activeClass: "text-sm font-semibold text-amber-500",
	},
	[ScheduleType.REST]: {
		label: "휴식",
		icon: ["fas", "mug-saucer"],
		iconClass: "text-emerald-600",
		chipClass: "border-emerald-200 bg-emerald-50 text-emerald-600",
		activeClass: "text-sm font-semibold text-emerald-500",
	},
	[ScheduleType.OTHER]: {
		label: "기타",
		icon: ["fas", "ellipsis"],
		iconClass: "text-purple-600",
		chipClass: "border-purple-200 bg-purple-50 text-purple-600",
		activeClass: "text-sm font-semibold text-purple-500",
	},
};

export const scheduleCategories = Object.entries(scheduleCategoryMap).map(
	([type, item]) => ({
		type: type as ScheduleType,
		...item,
	}),
);

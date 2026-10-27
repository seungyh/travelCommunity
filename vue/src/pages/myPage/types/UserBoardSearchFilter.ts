export type UserBoardCategory = "DOMESTIC" | "OVERSEAS" | "FOOD" | "ACCOMMODATION" | "TIP";

export interface UserBoardSearchFilter {
	category: UserBoardCategory | null;
	titleOrContent: string | null;
}
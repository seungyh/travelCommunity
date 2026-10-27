import dayjs from "dayjs";
import "dayjs/locale/ko"; // 한국어 로케일 불러오기

/**
 * 오늘로부터 ~일전 ~년전 문자열 반환
 *  */
export const getDiffDateFromToday = (createdAt: Date | null | undefined) => {
	if (!createdAt) {
		return "등록일 미존재";
	}
	const today = new Date();
	const createdDate = new Date(createdAt);

	if (today.getFullYear() - createdDate.getFullYear() > 0) {
		return `${today.getFullYear() - createdDate.getFullYear()}년 이상 전`;
	}
	if (today.getMonth() - createdDate.getMonth() > 0) {
		return `${today.getMonth() - createdDate.getMonth() + 1}개월 이상 전`;
	}
	if (today.getDate() - createdDate.getDate() > 0) {
		return `${today.getDate() - createdDate.getDate()}일 전`;
	}
	if (today.getHours() - createdDate.getHours() > 0) {
		return `${today.getHours() - createdDate.getHours()}시간 전`;
	}
	if (today.getMinutes() - createdDate.getMinutes() > 0) {
		return `${today.getMinutes() - createdDate.getMinutes()}분 전`;
	}
	if (today.getSeconds() - createdDate.getSeconds() > 0) {
		return `${today.getSeconds() - createdDate.getSeconds()}초 전`;
	}
	return "방금 전";
};

/**
 *
 * @param value yyyy-mm-dd를 yyyy년 mm월 dd일 (요일) 포맷으로 반환
 * @returns
 */
export const formatToFullDateWithDay = (
	value: string | Date | null | undefined,
) => {
	if (!value) {
		return "-";
	}

	const date = dayjs(value);

	// 유효하지 않은 날짜인 경우 원본 그대로 반환
	if (!date.isValid()) return value;

	// M과 D를 하나만 쓰면 01월 -> 1월로 패딩 없이 출력
	return date.format("YYYY년 M월 D일 (dd)");
};

/**
 * 시작 차이를 "0시간 0분"으로 반환
 * @param startTime - 00:00
 * @param endTime - 00:00
 */
export const getTimeDiff = (startTimeStr: string, endTimeStr: string) => {
	const [startHour, startMin] = startTimeStr.split(":").map(Number);
	const [endHour, endMin] = endTimeStr.split(":").map(Number);

	if (!startHour || !startMin || !endHour || !endMin) {
		return null;
	}
	// 변환된 값이 올바른 숫자인지 확인 (NaN 체크)
	if (
		isNaN(startHour) ||
		isNaN(startMin) ||
		isNaN(endHour) ||
		isNaN(endMin)
	) {
		return null;
	}
	// 분 단위로 변환 후 차이 계산 (시간 단위로 반환)
	const startTotalMinutes = startHour * 60 + startMin;
	const endTotalMinutes = endHour * 60 + endMin;

	const diffHour = Math.floor((endTotalMinutes - startTotalMinutes) / 60);
	const diffMin = (endTotalMinutes - startTotalMinutes) % 60;

	if (diffHour > 0) {
		return `${diffHour}시간 ${diffMin}분`;
	}
	return `${diffMin}분`;
};

/**
 * 시작일과 종료일의 유효성을 체크하고 종료일이 시작일보다 앞인지 체크
 * @param startDate
 * @param endDate
 */
export const dateValidCheck = (
	startDate: string | Date,
	endDate: string | Date,
) => {
	const start = dayjs(startDate);
	const end = dayjs(endDate);

	// 유효하지 않은 날짜인지 체크
	if (!start.isValid() || !end.isValid()) {
		return false;
	}

	// 종료일이 시작일보다 앞서면(이전이면) 유효하지 않음
	if (end.isBefore(start)) {
		return false;
	}

	return true;
};

/**
 * 오늘로부터 ~일전 ~년전 문자열 반환
 *  */
export const getDiffDateFromToday = (createdAt: Date) => {
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
};

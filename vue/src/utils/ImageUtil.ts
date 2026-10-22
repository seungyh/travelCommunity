// 프로필 이미지 경로 반환
export const getImageSrc = (path: string) => {
	return path.startsWith("http") || path.startsWith("blob")
		? path
		: `/web/api/account/profile/image?path=${encodeURIComponent(path)}`;
};

// 닉네임으로 아바타 생성
export const getAvatar = (nickName: string) => {
	const url = new URL("https://api.dicebear.com/10.x/lorelei/svg");
	url.searchParams.set("seed", nickName);
	url.searchParams.set("size", "50");

	return url.href;
};

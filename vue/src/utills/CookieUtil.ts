export const hasCookie = (name: string) => {
	return document.cookie
		.split("; ")
		.some((cookie) => cookie.startsWith(name));
};

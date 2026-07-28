import "./assets/main.css";
import "./assets/header.css";

import "./components/fontawesome/FontAwesome";

import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";

import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import axios from "axios";
import { createPinia } from "pinia";
import { useAuthStore } from "./stores/Auth.ts";

const app = createApp(App);
// 전역 컴포넌트 등록
app.component("font-awesome-icon", FontAwesomeIcon);

app.use(router).use(createPinia()).mount("#app");

const auth = useAuthStore();
axios.defaults.withCredentials = true;
axios.defaults.withXSRFToken = true;
function getCookie(name: string) {
	const matches = document.cookie.match(
		new RegExp(
			"(?:^|; )" +
				name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, "\\$1") +
				"=([^;]*)",
		),
	);
	return matches ? decodeURIComponent(matches[1]) : undefined;
}

// axios.interceptors.request.use((req: any) => {
// 	const config = req;

// 	config.headers["X-XSRF-TOKEN"] = getCookie("XSRF-TOKEN");

// 	return config;
// });

axios.interceptors.response.use(
	(response) => {
		return response;
	},
	(error) => {
		if (error.status === 401) {
			// 인증 에러 재로그인 필요 세션 삭제
			sessionStorage.removeItem("userId");
			auth.logout();
			console.log("logout ", auth.getLogin());
		}
		// error.status
		// thr
		// res.status
		throw error;
	},
);

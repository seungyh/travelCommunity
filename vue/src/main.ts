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

axios.defaults.withCredentials = true; // axios가 자체적으로 쿠키를 전송 jwt, xsrf token
axios.defaults.withXSRFToken = true; // axios가 자체적으로 XSRF-TOKEN을 읽어서 헤더에 넣음

axios.interceptors.response.use(
	(response) => {
		return response;
	},
	(error) => {
		if (error.status === 401) {
			// 인증 에러 재로그인 필요 세션 삭제
			sessionStorage.removeItem("userId");
			auth.setLogout();
			console.log("logout ", auth.getLogin());
		}
		// error.status
		// thr
		// res.status
		throw error;
	},
);

import "./assets/main.css";

import "./components/fontawesome/FontAwesome";

import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";

import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import axios, { AxiosError } from "axios";
import { createPinia } from "pinia";
import { useAuthStore } from "./stores/Auth.ts";
import type { ErrorResponse } from "./components/common/types/response/ErrorResponse.ts";
import { VueSpinnersPlugin } from "vue3-spinners";
// import Toast from "vue-toastification";
// import "vue-toastification/dist/index.css";
// import FloatingVue from "floating-vue";
// import "floating-vue/dist/style.css";
import dayjs from "dayjs";
import "dayjs/locale/ko"; // 한국어 로케일 import

const app = createApp(App);
// 전역 컴포넌트 등록
app.component("font-awesome-icon", FontAwesomeIcon);
app.use(VueSpinnersPlugin);
// app.use(FloatingVue);

// 전역 로케일을 한국어로 설정
dayjs.locale("ko");

// const options = {
// 	// toast 필요한 설정 옵션 (예: 지속 시간)
// 	timeout: 3000,
// };

// app.use(Toast, options);

app.use(router).use(createPinia()).mount("#app");

const auth = useAuthStore();

axios.defaults.withCredentials = true; // axios가 자체적으로 쿠키를 전송 jwt, xsrf token
axios.defaults.withXSRFToken = true; // axios가 자체적으로 XSRF-TOKEN을 읽어서 헤더에 넣음

axios.interceptors.response.use(
	(response) => {
		return response;
	},
	(error: AxiosError<ErrorResponse>) => {
		if (error.response?.status === 401) {
			alert("다시 로그인해주세요.");
			// 인증 에러 재로그인 필요 세션 삭제
			sessionStorage.removeItem("userId");
			auth.setLogout();
			router.push("/");
			return Promise.reject(error);
		}
		throw error;
	},
);

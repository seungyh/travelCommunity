import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";
import type { LoginRequest } from "@/pages/login/types/request/LoginRequest";
import type { LoginResponse } from "@/pages/login/types/response/LoginResponse";
import axios, { AxiosError, type AxiosResponse } from "axios";
import { defineStore } from "pinia";
import { ref } from "vue";
import router from "@/router";

export const useAuthStore = defineStore("auth", () => {
	const isLogin = ref(false);

	// 로그인 상태 저장
	const setLogin = () => {
		isLogin.value = true;
	};
	//로그아웃 상태 저장
	const setLogout = () => {
		isLogin.value = false;
	};

	// 로그인 상태 가져오기
	const getLogin = (): boolean => {
		return isLogin.value;
	};

	// 일반 로그인
	const login = (loginRequest: LoginRequest) => {
		axios
			.post("/web/api/account/login", loginRequest)
			.then((res: AxiosResponse<LoginResponse>) => {
				console.log("res", res);
				successLogin(res.data);
			})
			.catch((error: AxiosError<ErrorResponse>) => {
				console.log(error);
			});
	};
	// jwt 토큰으로 로그인
	const tokenLogin = () => {
		axios
			.get("/web/api/account/token/login")
			.then((res: AxiosResponse<LoginResponse>) => {
				console.log("res ", res);
				successLogin(res.data);
			})
			.catch((error: AxiosError<ErrorResponse>) => {
				console.log(error);
			});
	};
	// 로그인 성공 후 처리
	const successLogin = (res: LoginResponse) => {
		sessionStorage.setItem("userId", res.userId);
		setLogin();
		console.log("res success", res);
		router.push("/");
	};
	return {
		login,
		setLogin,
		setLogout,
		getLogin,
		tokenLogin,
	};
});

<template>
	<div class="w-full h-[100vh] flex flex-nowrap">
		<div class="login-left-area w-[50%] relative border-right bg-gray-300">
			<img
				src="../../assets/img/오로라.jpg"
				class="w-full h-full z-0"
				alt="로그인 배경"
			/>
			<div class="absolute text-white left-10 top-10 text-xl">
				커뮤니티
			</div>
			<div class="absolute bottom-[70px] w-[600px] left-[50px]">
				<span class="text-5xl font-bold text-white">
					"세상은 한 권의 책이다. 여행하지 않는 사람은 한 페이지만
					읽은 것이다."
				</span>
				<span class="text-white"> — 성 아우구스티누스 </span>
			</div>
		</div>
		<div class="login-right-area w-[50%] h-full">
			<div
				class="h-[500px] w-[40%]"
				style="margin-right: auto; margin-left: auto; margin-top: 30%"
			>
				<div
					class="w-full rounded-xl bg-gray-200 flex justify-around h-[40px]"
				>
					<div
						class="w-[48%] h-[30px] self-center pt-1 mr-2 text-center rounded-xl cursor-pointer"
						:class="isLoginMode ? 'bg-white' : 'text-gray-400'"
						@click="isLoginMode = true"
					>
						로그인
					</div>
					<div
						class="w-[48%] h-[30px] self-center pt-1 text-center rounded-xl cursor-pointer"
						:class="isLoginMode ? 'text-gray-400' : 'bg-white'"
						@click="isLoginMode = false"
					>
						회원가입
					</div>
				</div>
				<div>
					<div class="pt-7">
						<span class="text-2xl" style="font-weight: 700">
							<span v-if="isLoginMode"
								>다시 만나서 반가워요!</span
							>
							<span v-else>여행을 시작해볼까요?</span>
						</span>
					</div>
					<div class="w-full pt-1 text-gray-400">
						<span v-if="isLoginMode">
							여행 커뮤니티에 오신 걸 환영합니다.
						</span>
						<span v-else>
							계정을 만들고 여행 이야기를 나눠보세요.
						</span>
					</div>
					<div v-if="isLoginMode" class="w-full pt-3">
						<div class="relative">
							아이디
							<input
								v-model="loginInfo.userId"
								class="w-full h-[40px] rounded-md border pr-2 pl-10"
								placeholder="아이디를 입력해주세요."
							/>
							<font-awesome-icon
								class="absolute left-3 bottom-3 text-gray-500"
								size="md"
								:icon="['far', 'envelope']"
							/>
						</div>
						<div class="relative">
							비밀번호
							<input
								v-model="loginInfo.password"
								:type="isHidePwd ? 'password' : 'text'"
								class="w-full h-[40px] rounded-md border pr-10 pl-10"
								@keyup.enter="login"
							/>
							<font-awesome-icon
								class="absolute left-3 bottom-3 text-gray-500"
								size="md"
								icon="lock"
							/>
							<font-awesome-icon
								v-if="isHidePwd"
								@click="isHidePwd = !isHidePwd"
								class="absolute right-3 bottom-3 text-gray-500 cursor-pointer"
								size="md"
								icon="eye"
							/>
							<font-awesome-icon
								v-else
								@click="isHidePwd = !isHidePwd"
								class="absolute right-3 bottom-3 text-gray-500 cursor-pointer"
								size="md"
								icon="eye-slash"
							/>
						</div>
					</div>
					<div v-else class="w-full pt-3">
						<div class="relative">
							아이디
							<input
								v-model="signUpInfo.userId"
								class="w-full h-[40px] rounded-md border pr-2 pl-10"
								placeholder="아이디를 입력해주세요."
							/>
							<font-awesome-icon
								class="absolute left-3 bottom-3 text-gray-500"
								size="md"
								:icon="['far', 'envelope']"
							/>
						</div>
						<div class="relative">
							비밀번호
							<input
								v-model="signUpInfo.password"
								placeholder="비밀번호를 입력해주세요."
								:type="isHidePwd ? 'password' : 'text'"
								class="w-full h-[40px] rounded-md border pr-10 pl-10"
							/>
							<font-awesome-icon
								class="absolute left-3 bottom-3 text-gray-500"
								size="md"
								icon="lock"
							/>
							<font-awesome-icon
								v-if="isHidePwd"
								@click="isHidePwd = !isHidePwd"
								class="absolute right-3 bottom-3 text-gray-500 cursor-pointer"
								size="md"
								icon="eye"
							/>
							<font-awesome-icon
								v-else
								@click="isHidePwd = !isHidePwd"
								class="absolute right-3 bottom-3 text-gray-500 cursor-pointer"
								size="md"
								icon="eye-slash"
							/>
						</div>
						<div class="relative">
							비밀번호 확인
							<input
								v-model="pwCheck"
								placeholder="비밀번호를 다시 입력해주세요."
								class="w-full h-[40px] rounded-md border pr-10 pl-10"
							/>
							<!-- <font-awesome-icon
                            class="absolute left-3 bottom-3 text-gray-500"
                            size="md"
                            icon="lock" />
                            <font-awesome-icon
                            v-if="isHidePwd"
                            @click="isHidePwd = !isHidePwd"
                            class="absolute right-3 bottom-3 text-gray-500 cursor-pointer"
                            size="md"
                            icon="eye" />
                            <font-awesome-icon
                            v-else
                            @click="isHidePwd = !isHidePwd"
                            class="absolute right-3 bottom-3 text-gray-500 cursor-pointer"
                            size="md"
                            icon="eye-slash" /> -->
						</div>
						<div class="relative">
							이메일
							<input
								v-model="signUpInfo.email"
								class="w-full h-[40px] rounded-md border pr-2 pl-10"
								placeholder="example@example.com"
							/>
							<font-awesome-icon
								class="absolute left-3 bottom-3 text-gray-500"
								size="md"
								:icon="['far', 'envelope']"
							/>
						</div>
						<div class="relative">
							닉네임
							<input
								v-model="signUpInfo.nickName"
								class="w-full h-[40px] rounded-md border pr-2 pl-10"
								placeholder="커뮤니티에서 사용할 닉네임을 입력해주세요."
							/>
							<font-awesome-icon
								class="absolute left-3 bottom-3 text-gray-500"
								size="md"
								:icon="['far', 'envelope']"
							/>
						</div>
					</div>
					<div
						v-if="isLoginMode"
						class="text-teal-500 w-full text-right cursor-pointer"
						style="margin-top: 5px"
					>
						비밀번호를 잊으셨나요?
					</div>
					<div
						v-if="isLoginMode"
						class="cursor-pointer rounded-md bg-teal-500 text-white w-full text-center h-[40px] pt-2"
						style="margin-top: 10px"
					>
						<span @click="login">로그인</span>
					</div>
					<div
						v-else
						class="cursor-pointer rounded-md bg-teal-500 text-white w-full text-center h-[40px] pt-2"
						style="margin-top: 10px"
					>
						<span @click="signUp">회원가입</span>
					</div>

					<div class="flex justify-between pt-3 w-full">
						<div
							class="flex-1 border-t border-gray-200 self-center"
						></div>
						<div
							class="w-[50px] text-center text-gray-300"
							style="font-size: smaller"
						>
							또는
						</div>
						<div
							class="flex-1 border-t border-gray-200 self-center"
						></div>
					</div>
					<button @click="openKakaoLogin">kakao</button>
				</div>
			</div>
		</div>
	</div>
</template>
<script setup lang="ts">
import axios from "axios";
import { ref } from "vue";
import { emailCheck } from "@/utills/ValidCheck";
import { useAuthStore } from "@/stores/Auth.ts";
import type { LoginRequest } from "./types/request/LoginRequest";

const auth = useAuthStore();
const loginInfo = ref<LoginRequest>({
	userId: "",
	password: "",
});
const signUpInfo = ref({
	userId: "",
	email: "",
	password: "",
	nickName: "",
});
const pwCheck = ref("");
const isLoginMode = ref(true);
const isHidePwd = ref(true);
const openKakaoLogin = () => {
	// location.href = "http://localhost:8080/oauth2/authorization/kakao";
	location.href = `${import.meta.env.VITE_API_URL}/oauth2/authorization/kakao`;
};

const validCheck = () => {
	if (signUpInfo.value.password !== pwCheck.value) {
		alert("비밀번호 확인이 일치하지 않습니다.");
		return false;
	}
	if (!emailCheck(signUpInfo.value.email)) {
		alert("Email 형식이 올바르지 않습니다.");
		return false;
	}
	return true;
};
const login = () => {
	auth.login(loginInfo.value);
};
const signUp = () => {
	if (!validCheck()) {
		return;
	}
	axios
		.post("/web/api/account/sign-up", signUpInfo.value)
		.then(() => {
			alert("회원가입이 완료되었습니다.");
			isLoginMode.value = true;
		})
		.catch((error) => {
			console.log(error);
		});
};
</script>
<style scoped>
/* .login-left-area {

} */

.m-auto {
	margin: auto !important;
}
</style>

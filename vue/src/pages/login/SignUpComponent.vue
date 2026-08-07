<template>
	<div class="w-full pt-3">
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
				:type="isHidePwd[0] ? 'password' : 'text'"
				class="w-full h-[40px] rounded-md border pr-10 pl-10"
			/>
			<font-awesome-icon
				class="absolute left-3 bottom-3 text-gray-500"
				size="md"
				icon="lock"
			/>
			<font-awesome-icon
				v-if="!isHidePwd[0]"
				@click="isHidePwd[0] = !isHidePwd[0]"
				class="absolute right-3 bottom-3 text-gray-500 cursor-pointer"
				size="md"
				icon="eye"
			/>
			<font-awesome-icon
				v-else
				@click="isHidePwd[0] = !isHidePwd[0]"
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
				:type="isHidePwd[1] ? 'password' : 'text'"
				class="w-full h-[40px] rounded-md border pr-10 pl-10"
			/>
			<font-awesome-icon
				class="absolute left-3 bottom-3 text-gray-500"
				size="md"
				icon="lock"
			/>
			<font-awesome-icon
				v-if="!isHidePwd[1]"
				@click="isHidePwd[1] = !isHidePwd[1]"
				class="absolute right-3 bottom-3 text-gray-500 cursor-pointer"
				size="md"
				icon="eye"
			/>
			<font-awesome-icon
				v-else
				@click="isHidePwd[1] = !isHidePwd[1]"
				class="absolute right-3 bottom-3 text-gray-500 cursor-pointer"
				size="md"
				icon="eye-slash"
			/>
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
			<div class="w-full flex flex-row">
				<input
					v-model="signUpInfo.nickName"
					class="w-[79%] h-[40px] rounded-md border pr-2 pl-10"
					placeholder="닉네임을 입력해주세요."
				/>
				<font-awesome-icon
					class="absolute left-3 bottom-3 text-gray-500"
					size="md"
					:icon="['far', 'user']"
				/>
				<button
					class="h-[40px] w-[20%] ml-1! btn text-white border bg-green-600 disabled:bg-green-800 disabled:cursor-default cursor-pointer"
					:disabled="isGenerating"
					@click="getNickName()"
				>
					랜덤 생성
				</button>
			</div>
		</div>
	</div>
	<div
		class="cursor-pointer rounded-md bg-teal-500 text-white w-full text-center h-[40px] pt-2"
		style="margin-top: 10px"
	>
		<span @click="signUp">회원가입</span>
	</div>
</template>
<script setup lang="ts">
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";
import { emailCheck } from "@/utills/ValidCheck";
import axios, { AxiosError, type AxiosResponse } from "axios";
import { ref } from "vue";

const emit = defineEmits<{
	(e: "changeLoginMode"): void;
}>();
const isGenerating = ref(false);
const isHidePwd = ref([true, true]);
const pwCheck = ref("");
const signUpInfo = ref({
	userId: "",
	email: "",
	password: "",
	nickName: "",
});

const getNickName = () => {
	isGenerating.value = true; // 생성동안 버튼 비활성화, 생성후 1초동안 비활성화
	try {
		axios
			.get("/web/api/account/nickName")
			.then((res: AxiosResponse<string>) => {
				signUpInfo.value.nickName = res.data;
			})
			.catch((res: AxiosError<ErrorResponse>) => {
				console.log(res);
			});
	} finally {
		setTimeout(() => {
			isGenerating.value = false;
		}, 2000);
	}
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
const signUp = () => {
	if (!validCheck()) {
		return;
	}
	axios
		.post("/web/api/account/sign-up", signUpInfo.value)
		.then(() => {
			alert("회원가입이 완료되었습니다.");
			emit("changeLoginMode");
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			alert(error.response?.data.errorMessage);
			console.log(error);
		});
};
</script>

<template>
	<form class="grid gap-4" @submit.prevent="signUp">
		<template v-for="field in signUpFields" :key="field.key">
			<div>
				<label
					:for="`signup-${field.key}`"
					class="mb-2 block text-sm font-semibold text-gray-700"
					>{{ field.label }}</label
				>
				<div class="flex items-center gap-2">
					<div class="relative min-w-0 flex-1">
						<font-awesome-icon
							:icon="field.icon"
							class="pointer-events-none absolute top-1/2 left-4 -translate-y-1/2 text-gray-400"
						/>
						<input
							:id="`signup-${field.key}`"
							v-model="signUpInfo[field.key]"
							:type="
								field.key === 'password'
									? isHidePwd[0]
										? 'password'
										: 'text'
									: field.key === 'email'
										? 'email'
										: 'text'
							"
							:autocomplete="field.autocomplete"
							:placeholder="field.placeholder"
							class="h-12 w-full rounded-xl border border-gray-300 bg-white px-11 text-sm text-gray-900 outline-none placeholder:text-gray-400 focus:border-teal-500 focus:ring-4 focus:ring-teal-500/10"
							required
						/>
						<button
							v-if="field.key === 'password'"
							type="button"
							class="absolute top-1 right-1 size-10 cursor-pointer rounded-lg text-gray-500 hover:bg-gray-100"
							:aria-label="
								isHidePwd[0]
									? '비밀번호 표시'
									: '비밀번호 숨기기'
							"
							:aria-pressed="!isHidePwd[0]"
							@click="isHidePwd[0] = !isHidePwd[0]"
						>
							<font-awesome-icon
								:icon="isHidePwd[0] ? 'eye-slash' : 'eye'"
							/>
						</button>
					</div>
					<button
						v-if="field.key === 'nickName'"
						type="button"
						class="h-12 shrink-0 cursor-pointer rounded-xl border border-teal-100 bg-teal-50 px-3 text-xs font-semibold text-teal-700 hover:bg-teal-100 disabled:cursor-wait disabled:opacity-60"
						:disabled="isGenerating"
						@click="getNickName"
					>
						랜덤 생성
					</button>
				</div>
			</div>
			<div v-if="field.key === 'password'">
				<label
					for="signup-password-confirm"
					class="mb-2 block text-sm font-semibold text-gray-700"
					>비밀번호 확인</label
				>
				<div class="relative">
					<font-awesome-icon
						icon="lock"
						class="pointer-events-none absolute top-1/2 left-4 -translate-y-1/2 text-gray-400"
					/>
					<input
						id="signup-password-confirm"
						v-model="pwCheck"
						:type="isHidePwd[1] ? 'password' : 'text'"
						autocomplete="new-password"
						placeholder="비밀번호를 다시 입력해주세요"
						class="h-12 w-full rounded-xl border border-gray-300 bg-white px-11 text-sm text-gray-900 outline-none placeholder:text-gray-400 focus:border-teal-500 focus:ring-4 focus:ring-teal-500/10"
						required
					/>
					<button
						type="button"
						class="absolute top-1 right-1 size-10 cursor-pointer rounded-lg text-gray-500 hover:bg-gray-100"
						:aria-label="
							isHidePwd[1]
								? '비밀번호 확인 표시'
								: '비밀번호 확인 숨기기'
						"
						:aria-pressed="!isHidePwd[1]"
						@click="isHidePwd[1] = !isHidePwd[1]"
					>
						<font-awesome-icon
							:icon="isHidePwd[1] ? 'eye-slash' : 'eye'"
						/>
					</button>
				</div>
			</div>
		</template>
		<button
			type="submit"
			class="mt-1 h-12 w-full cursor-pointer rounded-xl bg-teal-600 text-sm font-semibold text-white shadow-sm hover:bg-teal-700"
		>
			회원가입
		</button>
	</form>
</template>
<script setup lang="ts">
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";
import { emailCheck } from "@/utils/ValidCheck";
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

const signUpFields = [
	{
		key: "userId",
		label: "아이디",
		icon: ["far", "user"],
		autocomplete: "username",
		placeholder: "아이디를 입력해주세요",
	},
	{
		key: "password",
		label: "비밀번호",
		icon: "lock",
		autocomplete: "new-password",
		placeholder: "비밀번호를 입력해주세요",
	},
	{
		key: "email",
		label: "이메일",
		icon: ["far", "envelope"],
		autocomplete: "email",
		placeholder: "example@example.com",
	},
	{
		key: "nickName",
		label: "닉네임",
		icon: ["far", "user"],
		autocomplete: "nickname",
		placeholder: "닉네임을 입력해주세요",
	},
] as const;
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

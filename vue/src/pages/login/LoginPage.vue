<template>
	<main
		class="grid min-h-dvh bg-white text-gray-800 md:grid-cols-[42%_58%] lg:grid-cols-2 [&_button]:focus-visible:outline-2 [&_button]:focus-visible:outline-offset-4 [&_button]:focus-visible:outline-teal-500 [&_a]:focus-visible:outline-2 [&_a]:focus-visible:outline-teal-500"
	>
		<aside
			class="sticky top-0 hidden h-dvh overflow-hidden bg-teal-950 text-white md:block"
			aria-label="여행 커뮤니티 소개"
		>
			<img
				:src="'/public/오로라.jpg'"
				class="absolute inset-0 size-full object-cover"
				alt="밤하늘을 물들이는 오로라"
			/>
			<div
				class="absolute inset-0 bg-linear-to-b from-slate-950/25 via-slate-950/10 to-slate-950/90"
			></div>
			<router-link
				to="/"
				class="absolute top-10 left-7 flex items-center gap-3 p-0! text-lg font-bold text-white! lg:left-12"
			>
				<span
					class="grid size-10 place-items-center rounded-xl border border-white/30 bg-white/10 text-2xl"
					><font-awesome-icon :icon="['far', 'compass']" /></span
				>여행 커뮤니티
			</router-link>
			<div
				class="absolute right-7 bottom-28 left-7 lg:right-12 lg:left-12"
			>
				<span
					class="mb-5 block text-xs font-bold tracking-[.18em] text-teal-200"
					>YOUR NEXT CHAPTER</span
				>
				<h2
					class="text-4xl leading-tight font-bold tracking-tight lg:text-5xl xl:text-6xl"
				>
					새로운 풍경,<br />함께하는 여행.
				</h2>
				<p
					class="mt-6 mb-7 text-sm leading-loose break-keep text-slate-200"
				>
					다음 여행을 계획하고, 소중한 순간을 나눠보세요.<br />당신의
					여행 이야기가 여기서 시작됩니다.
				</p>
				<div class="flex flex-wrap gap-2">
					<span
						v-for="tag in [
							'여행 계획',
							'여행 이야기',
							'함께하는 순간',
						]"
						:key="tag"
						class="rounded-full border border-white/20 bg-white/5 px-3 py-2 text-xs"
						>{{ tag }}</span
					>
				</div>
			</div>
			<span
				class="absolute bottom-8 left-7 text-xs text-slate-300 lg:left-12"
				>조금 더 멀리, 조금 더 특별하게.</span
			>
		</aside>
		<section
			class="flex min-h-dvh min-w-0 flex-col items-center bg-linear-to-br from-teal-50/60 via-white to-white p-6 md:bg-none md:px-8 md:py-9 lg:px-12"
		>
			<router-link
				to="/"
				class="inline-flex items-center gap-2 self-start p-0! text-sm text-gray-500! hover:text-teal-600!"
				><font-awesome-icon icon="arrow-left" />홈으로
				돌아가기</router-link
			>
			<div class="m-auto w-full max-w-[400px] pt-10 pb-6">
				<div
					class="flex gap-1 rounded-xl border border-gray-200 bg-gray-100 p-1"
					aria-label="계정 메뉴"
				>
					<button
						v-for="mode in modes"
						:key="mode.label"
						type="button"
						class="flex-1 cursor-pointer rounded-lg py-2.5 text-sm font-semibold transition-colors motion-reduce:transition-none"
						:class="
							isLoginMode === mode.login
								? 'bg-white text-gray-900 shadow-sm'
								: 'text-gray-500 hover:text-gray-700'
						"
						:aria-pressed="isLoginMode === mode.login"
						@click="isLoginMode = mode.login"
					>
						{{ mode.label }}
					</button>
				</div>
				<header class="mt-8 mb-7">
					<span
						class="mb-2.5 block text-[10px] font-bold tracking-[.18em] text-teal-600"
						>LET’S TRAVEL TOGETHER</span
					>
					<h1
						class="mb-2 text-[28px] leading-snug font-bold tracking-tight break-keep"
					>
						{{
							isLoginMode
								? "다시 만나서 반가워요!"
								: "여행을 시작해볼까요?"
						}}
					</h1>
					<p class="text-sm break-keep text-gray-500">
						{{
							isLoginMode
								? "로그인하고 나만의 다음 여행을 이어가세요."
								: "계정을 만들고 함께 여행 이야기를 나눠보세요."
						}}
					</p>
				</header>
				<form
					v-if="isLoginMode"
					class="grid gap-5"
					@submit.prevent="login"
				>
					<div v-for="field in loginFields" :key="field.key">
						<label
							:for="`login-${field.key}`"
							class="mb-2 block text-sm font-semibold text-gray-700"
							>{{ field.label }}</label
						>
						<div class="relative">
							<font-awesome-icon
								:icon="field.icon"
								class="pointer-events-none absolute top-1/2 left-4 -translate-y-1/2 text-gray-400"
							/>
							<input
								:id="`login-${field.key}`"
								v-model="loginInfo[field.key]"
								:name="field.autocomplete"
								:autocomplete="field.autocomplete"
								:type="
									field.key === 'password' && isHidePwd
										? 'password'
										: 'text'
								"
								:placeholder="`${field.label}를 입력해주세요`"
								class="h-12 w-full rounded-xl border border-gray-300 bg-white px-11 text-sm text-gray-900 shadow-xs outline-none transition-shadow placeholder:text-gray-400 focus:border-teal-500 focus:ring-4 focus:ring-teal-500/10 motion-reduce:transition-none"
								required
							/>
							<button
								v-if="field.key === 'password'"
								type="button"
								class="absolute top-1 right-1 size-10 cursor-pointer rounded-lg text-gray-500 hover:bg-gray-100"
								:aria-label="
									isHidePwd
										? '비밀번호 표시'
										: '비밀번호 숨기기'
								"
								:aria-pressed="!isHidePwd"
								@click="isHidePwd = !isHidePwd"
							>
								<font-awesome-icon
									:icon="isHidePwd ? 'eye-slash' : 'eye'"
								/>
							</button>
						</div>
					</div>
					<button
						type="submit"
						class="mt-1 flex h-12 w-full cursor-pointer items-center justify-center gap-3 rounded-xl bg-teal-600 text-sm font-semibold text-white shadow-sm transition-colors hover:bg-teal-700 motion-reduce:transition-none"
					>
						로그인<font-awesome-icon :icon="faArrowRight" />
					</button>
				</form>
				<sign-up-component
					v-else
					@changeLoginMode="isLoginMode = true"
				/>
				<div
					class="my-6 flex items-center gap-3 text-xs text-gray-400 before:h-px before:flex-1 before:bg-gray-200 after:h-px after:flex-1 after:bg-gray-200"
				>
					소셜 계정으로 간편하게
				</div>
				<div class="grid gap-2.5">
					<button
						v-for="provider in socialProviders"
						:key="provider.id"
						type="button"
						class="relative flex min-h-12 w-full items-center justify-center rounded-xl border px-18 py-3 text-sm font-semibold transition-colors disabled:cursor-not-allowed motion-reduce:transition-none"
						:class="provider.color"
						:disabled="!provider.enabled"
						:aria-describedby="
							provider.enabled ? undefined : 'social-notice'
						"
						@click="openSocialLogin(provider.id)"
					>
						<font-awesome-icon
							:icon="provider.icon"
							class="absolute left-4 size-5"
							:class="provider.iconColor"
						/>
						<span>{{ provider.label }}로 시작하기</span>
						<font-awesome-icon
							v-if="provider.enabled"
							:icon="faArrowRight"
							class="absolute right-4 opacity-60"
						/>
						<span
							v-else
							class="absolute right-3 rounded bg-black/5 px-1.5 py-0.5 text-[10px] font-normal"
							>준비 중</span
						>
					</button>
				</div>
				<p
					id="social-notice"
					class="mt-2.5 text-center text-[11px] text-gray-500"
				>
					구글 · 네이버 로그인은 곧 만나요.
				</p>
				<p
					class="mt-7 flex items-center justify-center gap-2 text-xs text-gray-400"
				>
					당신의 다음 여행, 함께 만들어가요.<font-awesome-icon
						icon="plane"
						class="text-teal-600"
					/>
				</p>
			</div>
		</section>
	</main>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { faGoogle } from "@fortawesome/free-brands-svg-icons";
import {
	faArrowRight,
	faComment,
	faN,
} from "@fortawesome/free-solid-svg-icons";
import { useAuthStore } from "@/stores/Auth.ts";
import type { LoginRequest } from "./types/request/LoginRequest";
import SignUpComponent from "./SignUpComponent.vue";

const auth = useAuthStore();
const loginInfo = ref<LoginRequest>({ userId: "", password: "" });
const isLoginMode = ref(true);
const isHidePwd = ref(true);
const modes = [
	{ label: "로그인", login: true },
	{ label: "회원가입", login: false },
];
const loginFields = [
	{
		key: "userId",
		label: "아이디",
		icon: ["far", "user"],
		autocomplete: "username",
	},
	{
		key: "password",
		label: "비밀번호",
		icon: "lock",
		autocomplete: "current-password",
	},
] as const;
// 카카오와 네이버는 Font Awesome의 말풍선 / N 아이콘으로 표현합니다.
const socialProviders = [
	{
		id: "kakao",
		label: "카카오",
		icon: faComment,
		enabled: true,
		color: "cursor-pointer border-transparent bg-[#fee500] text-[#191919] hover:bg-[#f5dc00]",
		iconColor: "",
	},
	{
		id: "google",
		label: "Google",
		icon: faGoogle,
		enabled: false,
		color: "border-gray-300 bg-white text-gray-700",
		iconColor: "text-blue-500",
	},
	{
		id: "naver",
		label: "네이버",
		icon: faN,
		enabled: false,
		color: "border-transparent bg-[#03c75a] text-white",
		iconColor: "",
	},
];
const openSocialLogin = (provider: string) => {
	if (provider !== "kakao") return;
	location.href = `${import.meta.env.VITE_API_URL}/oauth2/authorization/${provider}`;
};
const login = () => {
	auth.login(loginInfo.value);
};
</script>

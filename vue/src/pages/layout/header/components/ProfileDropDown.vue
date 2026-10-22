<template>
	<div class="profile-img" @click.stop>
		<div class="absolute">
			<img
				v-if="auth.getProfileImagePath()"
				class="rounded-full w-12 h-12"
				:src="getImageSrc(auth.getProfileImagePath())"
			/>
			<img
				v-else
				:src="getAvatar(auth.getNickName())"
				alt="Avatar"
				class="rounded-full w-12 h-12 bg-gray-300"
			/>
			<Transition>
				<div
					v-if="openDropdownType == DropdownType.PROFILE"
					class="dropdown-menu"
				>
					<div
						class="pl-4 pr-4 pb-2 border-b border-gray-200 cursor-default"
					>
						<span class="block w-full font-bold">{{
							auth.getNickName()
						}}</span>
						<span class="block w-full text-sm text-gray-400">{{
							auth.getEmail() ?? "이메일을 등록해주세요."
						}}</span>
					</div>
					<div @click="router.push('/my-page')" class="dropdown-item">
						<font-awesome-icon :icon="['far', 'user']" />
						마이 페이지
					</div>

					<div class="dropdown-item">
						<font-awesome-icon :icon="['fas', 'gear']" />
						프로필 편집
					</div>

					<div
						@click="logout"
						class="dropdown-item border-t border-gray-200 text-red-500"
					>
						<font-awesome-icon
							:icon="['fas', 'right-from-bracket']"
						/>
						로그아웃
					</div>
				</div>
			</Transition>
		</div>
	</div>
</template>
<script setup lang="ts">
import { DropdownType } from "../enmus/DropdownType";
import axios, { AxiosError, type AxiosResponse } from "axios";
import type { CommonResponse } from "@/components/common/types/response/CommonResponse";
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";
import { useAuthStore } from "@/stores/Auth";
import { useRoute, useRouter } from "vue-router";
import { getAvatar, getImageSrc } from "@/utils/ImageUtil.ts";

const router = useRouter();
const route = useRoute();
const props = defineProps(["userId", "openDropdownType"]);

const auth = useAuthStore();

// 로그아웃
const logout = () => {
	axios
		.post("/web/api/account/logout")
		.then((res: AxiosResponse<CommonResponse>) => {
			if (res.data.result) {
				logoutProcess();
			}
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			console.log(error);
		});
};
// 로그아웃 처리
const logoutProcess = () => {
	auth.setLogout();
	let isRequireAuth = router.currentRoute.value.matched.some(
		(route) => route.meta.requiresAuth === true,
	);

	if (isRequireAuth) {
		router.push("/");
	}
};
</script>
<style scoped>
.profile-img {
	position: relative;
	border-radius: 25px;
	width: 50px;
	height: 50px;
	cursor: pointer;
}
</style>

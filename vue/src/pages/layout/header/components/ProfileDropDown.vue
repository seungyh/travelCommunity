<template>
	<div class="profile-img" @click.stop>
		<div>
			<img :src="src" alt="Avatar" />
			<Transition>
				<div
					v-if="openDropdownType == DropdownType.PROFILE"
					class="dropdown-menu"
				>
					<div
						class="pl-4 pr-4 pb-2 border-b border-gray-200 cursor-default"
					>
						<span class="block w-full font-bold">홍길동</span>
						<span class="block w-full text-sm text-gray-400"
							>test@test.com</span
						>
					</div>
					<div class="dropdown-item">
						<font-awesome-icon :icon="['far', 'user']" />
						마이 페이지
					</div>

					<div class="dropdown-item">
						<font-awesome-icon :icon="['fas', 'gear']" />
						설정
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
import { computed, ref } from "vue";
import { DropdownType } from "../types/DropdownType";
import axios, { AxiosError, type AxiosResponse } from "axios";
import type { CommonResponse } from "@/components/common/types/response/CommonResponse";
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";

const props = defineProps(["userId", "openDropdownType"]);

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
	sessionStorage.removeItem("userId");
};
const src = computed(() => {
	const url = new URL("https://api.dicebear.com/10.x/lorelei/svg");
	url.searchParams.set("seed", props.userId);
	url.searchParams.set("size", "50");

	return url.href;
});
</script>
<style scoped>
.profile-img {
	border-radius: 25px;
	width: 50px;
	height: 50px;
	border: 1px solid black;
	cursor: pointer;
}
</style>

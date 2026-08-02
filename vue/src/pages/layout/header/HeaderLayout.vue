<template>
	<div class="header-area">
		<div class="logo-area">
			<span class="title">여행 커뮤니티</span>
		</div>
		<div class="header-menu-area absolute left-3/7">
			<div class="mr-[5px]!">
				<span
					class="btn w-[60px] text-center p-[2px]"
					:class="[
						route.path === '/'
							? 'bg-teal-500 text-white'
							: 'hover:bg-gray-100',
					]"
				>
					<!-- 집 아이콘 -->
					<font-awesome-icon :icon="['far', 'house']" /> 홈</span
				>
			</div>

			<div class="mr-[5px]!">
				<span
					class="btn w-[80px] hover:bg-gray-100 text-center p-[2px]"
					:class="[
						route.path === '/post'
							? 'bg-teal-500 text-white'
							: 'hover:bg-gray-100',
					]"
				>
					<!-- 신문 아이콘 -->
					<font-awesome-icon
						:icon="['far', 'newspaper']"
					/>게시글</span
				>
			</div>
			<div>
				<span class="btn w-[80px] hover:bg-gray-100 text-center p-[2px]"
					><font-awesome-icon :icon="['far', 'compass']" />여행
					<!-- 나침반 아이콘 -->
					<font-awesome-icon icon="chevron-down" />
				</span>
			</div>
		</div>
		<div class="header-menu-right-area absolute right-1/20">
			<div
				class="btn hover:bg-gray-100 w-[30px] text-center content-center mr-[10px]!"
			>
				<font-awesome-icon icon="magnifying-glass" />
			</div>
			<div v-if="auth.getLogin()" class="flex flex-nowrap items-center">
				<router-link to="/">
					<span
						class="btn text-center mr-[15px]! p-[2px] bg-teal-500 w-[100px] hover:bg-teal-600 text-white"
						><font-awesome-icon
							icon="pencil"
							class="mr-[5px]!"
						/>글쓰기</span
					></router-link
				>
				<bell-drop-down
					@click="dropdownType = DropdownType.BELL"
					:open-dropdown-type="dropdownType"
				/>
				<profile-drop-down
					@click="dropdownType = DropdownType.PROFILE"
					:open-dropdown-type="dropdownType"
					:user-id="userId"
				/>
			</div>
			<div v-else class="mr-[10px]!">
				<router-link to="/login">
					<span
						class="btn text-center p-[2px] bg-teal-500 w-[100px] hover:bg-teal-600 text-white"
						><font-awesome-icon
							icon="arrow-right-to-bracket"
							class="mr-[5px]!"
						/>로그인</span
					></router-link
				>
			</div>
		</div>
	</div>
</template>
<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import BellDropDown from "./components/BellDropDown.vue";
import ProfileDropDown from "./components/ProfileDropDown.vue";
import { DropdownType } from "./types/DropdownType.ts";
import { useAuthStore } from "@/stores/Auth.ts";
import { hasCookie } from "@/utills/CookieUtil.ts";
import axios, { AxiosError } from "axios";

const route = useRoute();
const userId = ref<string | null>(null);
const dropdownType = ref(DropdownType.NONE); // 드롭다운 메뉴
const auth = useAuthStore();
// 바탕 클릭 시 드롭다운 닫음
const handleBodyClick = () => {
	dropdownType.value = DropdownType.NONE;
};

onMounted(() => {
	// CSRF 토큰 없으면 서버에서 생성
	if (!hasCookie("XSRF-TOKEN=")) {
		axios.get("/web/api/account/csrf");
	}
	auth.tokenLogin();
	window.addEventListener("click", handleBodyClick);
});
</script>

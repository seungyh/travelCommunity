<template>
	<div class="header-area top-0 fixed z-999">
		<div class="logo-area">
			<span class="title">여행 커뮤니티</span>
		</div>
		<div class="header-menu-area left-3/7">
			<div @click="router.push('/')" class="mr-[5px]!">
				<span
					class="btn w-[60px] p-[2px]"
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
					class="btn w-[80px] hover:bg-gray-100 p-[2px]"
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
			<div class="relative">
				<span
					@click.stop="dropdownType = DropdownType.TRAVEL"
					class="btn w-[80px] hover:bg-gray-100 p-[2px]"
					:class="
						dropdownType === DropdownType.TRAVEL
							? 'text-teal-600 bg-teal-50'
							: ''
					"
					><font-awesome-icon :icon="['far', 'compass']" />여행
					<!-- 나침반 아이콘 -->
					<font-awesome-icon
						icon="chevron-down"
						:class="
							dropdownType === DropdownType.TRAVEL
								? '-rotate-180 transition-transform duration-300'
								: '-rotate-0 transition-transform duration-300'
						"
					/>
				</span>
				<Transition class="w-[250px]">
					<div
						v-if="dropdownType === DropdownType.TRAVEL"
						class="dropdown-menu left-0"
					>
						<div
							@click.stop="moveToTravelPlan"
							class="dropdown-item"
						>
							<div
								class="bg-teal-50 rounded-lg w-[30px] h-[30px] content-center text-center"
							>
								<i class="bi bi-geo-alt-fill"></i>
							</div>
							<div>
								<div>여행 계획</div>
								<div class="text-xs text-gray-400">
									일정을 시간대별로 계획하세요
								</div>
							</div>
						</div>

						<div class="dropdown-item">
							<div
								class="bg-teal-50 rounded-lg w-[30px] h-[30px] content-center text-center"
							>
								<font-awesome-icon
									class="text-gray-600 text-md"
									:icon="['far', 'comment']"
								/>
							</div>
							<div>
								<div>채팅</div>
								<div class="text-xs text-gray-400">
									여행자들과 소통하세요
								</div>
							</div>
						</div>
					</div>
				</Transition>
			</div>
		</div>
		<div
			class="header-menu-right-area right-1/20"
			:class="auth.getLogin() ? 'w-[270px]' : 'w-[170px]'"
		>
			<div
				class="btn hover:bg-gray-100 w-[30px] content-center mr-[10px]!"
			>
				<font-awesome-icon icon="magnifying-glass" />
			</div>
			<div v-if="auth.getLogin()" class="flex flex-nowrap items-center">
				<router-link to="/board/write">
					<span
						class="btn mr-[15px]! p-[2px] bg-teal-500 w-[100px] hover:bg-teal-600 text-white"
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
						class="btn p-[2px] bg-teal-500 w-[100px] hover:bg-teal-600 text-white"
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
import { useRoute, useRouter } from "vue-router";
import BellDropDown from "./components/BellDropDown.vue";
import ProfileDropDown from "./components/ProfileDropDown.vue";
import { DropdownType } from "./enmus/DropdownType.ts";
import { useAuthStore } from "@/stores/Auth.ts";

const route = useRoute();
const router = useRouter();
const auth = useAuthStore();
const dropdownType = ref(DropdownType.NONE); // 드롭다운 메뉴
const userId = ref<string | null>(null);

const moveToTravelPlan = () => {
	if (!auth.getLogin()) {
		alert("로그인 후 이용 가능한 서비스입니다.");
		return;
	}
	router.push("/travel/plan/list");
};

// 바탕 클릭 시 드롭다운 닫음
const handleBodyClick = () => {
	dropdownType.value = DropdownType.NONE;
};

onMounted(() => {
	auth.tokenLogin();
	window.addEventListener("click", handleBodyClick);
});
</script>

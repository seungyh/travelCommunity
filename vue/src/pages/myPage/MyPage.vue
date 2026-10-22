<template>
	<div class="min-h-screen bg-[#f8fafc]">
		<!-- 커버 -->
		<div
			class="w-full aspect-[1200/223] max-h-[223px] overflow-hidden cursor-pointer"
			@click="openModal(modalType.BACKGROUND)"
		>
			<img
				:src="
					myInfo.backgroundPath
						? getImageSrc(myInfo.backgroundPath)
						: '/public/구름.jpg'
				"
				class="w-full h-full object-cover"
			/>
		</div>

		<!-- 프로필 영역 -->
		<section class="bg-white border-b border-gray-200">
			<div class="max-w-[980px] mx-auto relative">
				<!-- 프로필 사진 -->
				<div
					class="absolute -top-[55px] left-0 w-[112px] h-[112px] rounded-full border-4 border-white shadow-md overflow-visible bg-white"
				>
					<img
						:src="
							myInfo.profilePath
								? getImageSrc(myInfo.profilePath)
								: getAvatar(auth.getNickName())
						"
						class="w-full h-full rounded-full object-cover"
					/>

					<button
						v-if="myInfo.userId === auth.getUserId()"
						@click="openModal(modalType.IMAGE)"
						class="absolute bottom-0 right-0 w-8 h-8 bg-teal-500 text-white rounded-full flex items-center justify-center border-2 border-white cursor-pointer"
					>
						<font-awesome-icon :icon="['fas', 'camera']" />
					</button>
				</div>

				<!-- 버튼 -->
				<div
					v-if="myInfo.userId === auth.getUserId()"
					class="absolute right-0 top-4 flex gap-2"
				>
					<div class="content-center">
						<font-awesome-icon
							@click="reCreateInviteCode"
							:icon="['fas', 'refresh']"
							class="text-gray-500 cursor-pointer"
						/>
					</div>
					<button
						@click="copyInviteCode"
						class="flex items-center gap-2 h-[36px] px-3 rounded-md bg-gray-100 text-xs text-gray-500 hover:bg-gray-200 transition-colors cursor-pointer"
					>
						<span>초대코드</span>

						<span class="font-semibold text-gray-700">
							#{{ myInfo?.inviteCode }}
						</span>

						<font-awesome-icon
							:icon="['far', 'copy']"
							class="text-gray-500"
						/>
					</button>
					<button
						@click="openModal(modalType.PROFILE)"
						class="h-9 px-4 rounded-lg border border-gray-300 text-gray-700 text-sm font-semibold bg-white hover:bg-gray-50 cursor-pointer"
					>
						<font-awesome-icon :icon="['fas', 'gear']" />
						프로필 편집
					</button>
				</div>

				<!-- 사용자 정보 -->
				<div class="pt-[72px]">
					<h1 class="text-[23px] font-bold text-gray-900">
						{{ myInfo?.nickName }}
					</h1>

					<p class="mt-1 text-[13px] text-gray-600">
						{{ myInfo?.bio }}
					</p>

					<div
						class="flex items-center gap-4 mt-3 text-[12px] text-gray-500"
					>
						<span>
							<i class="fa-regular fa-calendar mr-1"></i>
							{{ dayjs(myInfo?.createdAt).format("YYYY년 MM월") }}
							가입
						</span>
					</div>

					<!-- 통계 -->
					<div class="flex mt-4">
						<div class="pr-6">
							<div class="font-bold text-[20px]">
								{{ myInfo?.boardCount }}
							</div>
							<div class="text-xs text-gray-500">게시글</div>
						</div>

						<div class="px-6 border-l border-gray-200">
							<div class="font-bold text-[20px]">
								{{ myInfo?.followerCount.toLocaleString() }}
							</div>
							<div class="text-xs text-gray-500">팔로워</div>
						</div>

						<div class="px-6 border-l border-gray-200">
							<div class="font-bold text-[20px]">
								{{ myInfo?.followingCount.toLocaleString() }}
							</div>
							<div class="text-xs text-gray-500">팔로잉</div>
						</div>

						<div class="px-6 border-l border-gray-200">
							<div class="font-bold text-[20px]">
								{{ myInfo?.allLikeCount.toLocaleString() }}
							</div>
							<div class="text-xs text-gray-500">받은 좋아요</div>
						</div>
					</div>
				</div>

				<!-- 탭 -->
				<div class="flex gap-1 mt-7">
					<button
						@click="selectedTag = TabValue.BOARD"
						class="px-4 h-[48px] border-b-2 text-sm cursor-pointer"
						:class="
							selectedTag === TabValue.BOARD
								? ' border-teal-500 text-teal-600 font-semibold'
								: 'hover:text-gray-900 border-transparent text-gray-600'
						"
					>
						<i class="fa-regular fa-rectangle-list mr-2"></i>
						내 게시글
					</button>

					<button
						@click="selectedTag = TabValue.FOLLOWER"
						class="px-4 h-[48px] border-b-2 text-sm cursor-pointer"
						:class="
							selectedTag === TabValue.FOLLOWER
								? ' border-teal-500 text-teal-600 font-semibold'
								: 'hover:text-gray-900 border-transparent text-gray-600'
						"
					>
						<i class="fa-solid fa-user-plus mr-2"></i>
						팔로워
					</button>

					<button
						@click="selectedTag = TabValue.FOLLOWING"
						class="px-4 h-[48px] border-b-2 text-sm cursor-pointer"
						:class="
							selectedTag === TabValue.FOLLOWING
								? ' border-teal-500 text-teal-600 font-semibold'
								: 'hover:text-gray-900 border-transparent text-gray-600'
						"
					>
						<i class="fa-solid fa-user-check mr-2"></i>
						팔로잉
					</button>
				</div>
				<UserBoardListTab
					v-if="selectedTag === TabValue.BOARD"
				></UserBoardListTab>
				<FollowListTab
					v-if="
						selectedTag === TabValue.FOLLOWER ||
						selectedTag === TabValue.FOLLOWING
					"
					:search-type="selectedTag"
					:user-id="myInfo.userId"
				></FollowListTab>
			</div>
		</section>

		<!-- 프로필 편집 모달 -->
		<ProfileModModal
			v-if="isModalOpen === modalType.PROFILE"
			@close="closeModal"
		></ProfileModModal>

		<ProfileImageModModal
			v-if="isModalOpen === modalType.IMAGE"
			:image-path="myInfo.profilePath"
			@close="closeModal"
		></ProfileImageModModal>
		<BackgroundImageModModal
			v-if="isModalOpen === modalType.BACKGROUND"
			@close="closeModal"
			:image-path="myInfo.backgroundPath"
		></BackgroundImageModModal>
	</div>
</template>
<script setup lang="ts">
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";
import { useSpinnerStore } from "@/stores/Spinner";
import axios, { AxiosError, type AxiosResponse } from "axios";
import { onMounted, ref } from "vue";
import UserBoardListTab from "./tabs/UserBoardListTab.vue";
import FollowListTab from "./tabs/FollowListTab.vue";
import { TabValue } from "./enmus/TabValue.ts";
import type { MyPageResponse } from "./types/response/MyPageResponse.ts";
import ProfileModModal from "./modal/ProfileModModal.vue";
import dayjs from "dayjs";
import ProfileImageModModal from "./modal/ProfileImageModModal.vue";
import BackgroundImageModModal from "./modal/BackgroundImageModModal.vue";
import { getAvatar, getImageSrc } from "@/utils/ImageUtil.ts";
import { useAuthStore } from "@/stores/Auth.ts";
import { useRoute } from "vue-router";

const { startSpinner, endSpinner } = useSpinnerStore();
const auth = useAuthStore();
const route = useRoute();
// 프로필 편집 모달
const enum modalType {
	PROFILE,
	IMAGE,
	BACKGROUND,
}
const isModalOpen = ref<modalType | null>(null);
// 선택한 탭
const selectedTag = ref<TabValue>(TabValue.BOARD);
const myInfo = ref<MyPageResponse>({
	userId: "",
	nickName: "",
	email: "",
	createdAt: "",
	bio: "",
	inviteCode: "",
	boardCount: 0,
	allLikeCount: 0,
	followingCount: 0,
	followerCount: 0,
	profilePath: "",
	backgroundPath: "",
});
const closeModal = (reSearch: boolean) => {
	if (reSearch) {
		getUserInfo(reSearch);
	}
	isModalOpen.value = null;
};
const openModal = (type: modalType) => {
	if (auth.getUserId() !== route.params.userId) {
		return;
	}
	isModalOpen.value = type;
};
const getUserInfo = (reSearch: boolean) => {
	console.log("reSearch", reSearch);
	startSpinner();

	axios
		.get(`/web/api/account/my-info/${route.params.userId}`)
		.then((res: AxiosResponse<MyPageResponse>) => {
			if (res.data) {
				myInfo.value = res.data;
				if (reSearch) {
					auth.setProfileImagePath(myInfo.value.profilePath);
					auth.setNickName(myInfo.value.nickName);
				}
			}
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			alert(error.response?.data.errorMessage);
		})
		.finally(() => {
			endSpinner();
		});
};
const reCreateInviteCode = () => {
	if (!confirm("초대 코드를 재생성하시겠습니까?")) {
		return;
	}
	if (auth.getUserId() !== route.params.userId) {
		return;
	}
	startSpinner();
	axios
		.patch("/web/api/account/invite-code")
		.then((res: AxiosResponse<string>) => {
			if (myInfo.value) {
				myInfo.value.inviteCode = res.data;
			}
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			alert(error.response?.data.errorMessage);
		})
		.finally(() => {
			endSpinner();
		});
};

// 초대코드 복사
const copyInviteCode = async () => {
	if (myInfo.value && myInfo.value.inviteCode) {
		await navigator.clipboard.writeText(myInfo.value.inviteCode);
		alert("#" + myInfo.value.inviteCode + "가 복사되었습니다");
	} else {
		alert("초대 코드가 존재하지 않습니다.");
	}
};
onMounted(() => {
	console.log("??? ", route.params);
	getUserInfo(false);
	// getUserBoardList();
});
</script>

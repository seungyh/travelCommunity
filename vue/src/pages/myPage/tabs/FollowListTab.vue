<template>
	<div class="max-w-[980px] mx-auto py-8">
		<!-- 제목 -->
		<div class="flex items-center gap-2 mb-5">
			<h2 class="text-lg font-bold text-gray-900">
				{{ props.searchType === "FOLLOWER" ? "팔로워" : "팔로잉" }}
			</h2>

			<span class="text-sm text-gray-400">
				{{ totalCount.toLocaleString() }}명
			</span>
		</div>

		<!-- 사용자 목록 -->
		<div
			v-if="followList && followList.length > 0"
			class="grid grid-cols-2 gap-4"
		>
			<div
				v-for="(user, index) in followList"
				:key="user.userId"
				class="flex items-center justify-between bg-white border border-gray-200 rounded-xl px-5 py-4 transition-shadow hover:shadow-md"
			>
				<!-- 사용자 정보 -->
				<div class="flex items-center min-w-0 cursor-pointer">
					<!-- 프로필 -->
					<img
						:src="user.profilePath ?? getAvatar(user.nickName)"
						class="w-[56px] h-[56px] rounded-full object-cover flex-shrink-0"
					/>

					<div class="ml-4 min-w-0">
						<div
							class="text-[15px] font-semibold text-gray-900 hover:underline"
						>
							{{ user.nickName }}
						</div>

						<div class="mt-1 text-[13px] text-gray-500 truncate">
							{{ user.bio }}
						</div>
					</div>
				</div>
				<div
					v-if="
						props.searchType === 'FOLLOWING' &&
						props.userId === auth.getUserId()
					"
				>
					<!-- 팔로우 버튼 -->
					<button
						v-if="user.isFollow"
						@click="toggleFollow(user)"
						class="cursor-pointer ml-4 flex-shrink-0 h-[34px] px-4 rounded-lg border border-gray-300 bg-white text-[13px] font-medium text-gray-600 hover:bg-gray-50"
					>
						팔로잉
					</button>

					<button
						v-else
						@click="toggleFollow(user)"
						class="cursor-pointer ml-4 flex-shrink-0 h-[34px] px-4 rounded-lg bg-teal-500 text-[13px] font-medium text-white hover:bg-teal-600"
					>
						팔로우
					</button>
				</div>
				<div
					v-if="
						props.searchType === 'FOLLOWER' &&
						props.userId === auth.getUserId()
					"
				>
					<!-- 팔로우 삭제 버튼 -->
					<button
						@click="deleteFollower(user.userId, index)"
						class="cursor-pointer h-[34px] px-3 rounded-md border border-gray-300 bg-white text-[13px] font-medium text-gray-600 transition-colors hover:border-red-300 hover:bg-red-50 hover:text-red-500"
					>
						삭제
					</button>
				</div>
			</div>
		</div>
		<div
			v-if="followList.length === 0"
			class="flex flex-col items-center justify-center py-14"
		>
			<div
				class="flex items-center justify-center w-12 h-12 mb-3 rounded-full bg-gray-100 text-gray-400"
			>
				<font-awesome-icon
					:icon="
						props.searchType === 'FOLLOWER'
							? ['fas', 'user-group']
							: ['fas', 'user-plus']
					"
					class="text-lg"
				/>
			</div>

			<p class="text-sm font-medium text-gray-600">
				{{
					props.searchType === "FOLLOWER"
						? "아직 팔로워가 없습니다."
						: "아직 팔로우한 사용자가 없습니다."
				}}
			</p>

			<p class="mt-1 text-xs text-gray-400">
				{{
					props.searchType === "FOLLOWER"
						? "다른 사용자와 여행 이야기를 나눠보세요."
						: "마음에 드는 여행자를 팔로우해보세요."
				}}
			</p>
		</div>
	</div>
</template>
<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import { TabValue } from "../enmus/TabValue";
import type { FollowListRequest } from "../types/request/FollowListRequest";
import { useSpinnerStore } from "@/stores/Spinner";
import axios, { AxiosError, type AxiosResponse } from "axios";
import type { FollowListResponse } from "../types/response/FollowListResponse";
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";
import type { FollowInfo } from "../types/FollowInfo";
import { getAvatar } from "@/utils/ImageUtil";
import { useAuthStore } from "@/stores/Auth";

const auth = useAuthStore();
const props = defineProps(["searchType", "userId"]);
const { startSpinner, endSpinner } = useSpinnerStore();

const requestInfo = computed<FollowListRequest>(() => {
	return { userId: props.userId, tabType: props.searchType };
});
const followList = ref<FollowInfo[]>([]);
const totalCount = ref<number>(0); // 총 건수
const followLoading = ref<boolean>(false);

// 팔로워 또는 팔로잉 목록 조회
const getFollowInfo = () => {
	console.log("requestInfo.value", requestInfo.value);
	startSpinner();
	axios
		.post("/web/api/account/follow-list", requestInfo.value)
		.then((res: AxiosResponse<FollowListResponse>) => {
			// follow를 true로 만들어서 넣기
			followList.value = res.data.followList.map((info) => {
				return {
					...info,
					isFollow: true,
				};
			});
			totalCount.value = res.data.totalCount;
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			alert(error.response?.data.errorMessage);
		})
		.finally(() => {
			endSpinner();
		});
};

// 팔로우 토글
const toggleFollow = (user: FollowInfo) => {
	// 나의 마이 페이지가 아니면 리턴
	if (auth.getUserId() !== props.userId) {
		return;
	}
	if (!followLoading.value) {
		followLoading.value = true;
		axios
			.patch(`/web/api/board/follow/${user.userId}`)
			.then(() => {
				user.isFollow = !user.isFollow;
			})
			.catch((res: AxiosError<ErrorResponse>) => {
				alert(res.response?.data.errorMessage);
			})
			.finally(() => {
				followLoading.value = false;
			});
	}
};

// 팔로워 삭제
const deleteFollower = (userId: string, index: number) => {
	// 나의 마이 페이지가 아니면 리턴
	if (auth.getUserId() !== props.userId) {
		return;
	}
	if (!confirm("해당 팔로워를 정말로 삭제하시겠습니까?")) {
		return;
	}
	if (!followLoading.value) {
		followLoading.value = true;
		axios
			.delete(`/web/api/account/follower/${userId}`)
			.then(() => {
				followList.value.splice(index, 1);
			})
			.catch((res: AxiosError<ErrorResponse>) => {
				alert(res.response?.data.errorMessage);
			})
			.finally(() => {
				followLoading.value = false;
			});
	}
};
watch(
	() => props.searchType,
	() => {
		getFollowInfo();
	},
);
onMounted(() => {
	getFollowInfo();
});
</script>

<template>
	<!-- 상단 고정 영역 -->
	<div class="fixed top-16 z-20 w-[1278px] bg-white border-b border-gray-200">
		<!-- 여행 정보 -->
		<div class="h-[100px] px-6 flex items-center justify-between">
			<div>
				<div class="flex items-center gap-2">
					<h1 class="text-3xl font-bold">
						{{
							planInfo?.title ??
							"이번 여행의 제목을 입력해보세요."
						}}
					</h1>

					<button class="text-gray-400 hover:text-teal-500">
						<font-awesome-icon :icon="['fas', 'pen']" />
					</button>
				</div>

				<p class="mt-2 text-sm text-gray-500">
					여행 동료와 함께 여행 계획을 작성해보세요
				</p>
			</div>

			<div class="flex items-center gap-5">
				<!-- 참여자 -->
				<div class="flex items-center">
					<img
						v-for="(user, index) in connectedUsers.slice(0, 3)"
						:key="user.userId"
						:src="getImageSrc(user.profilePath)"
						class="w-9 h-9 rounded-full border-2 border-white"
						:class="index > 0 ? '-ml-2' : ''"
					/>

					<div
						v-if="connectedUsers.length > 5"
						class="w-9 h-9 rounded-full bg-gray-100 border-2 border-white -ml-2 flex items-center justify-center text-gray-500"
					>
						+{{ connectedUsers.length - 5 }}
					</div>
				</div>

				<!-- 초대 -->
				<button
					class="bg-teal-500 hover:bg-teal-600 text-white px-5 h-[42px] rounded-lg cursor-pointer"
				>
					<font-awesome-icon
						:icon="['fas', 'user-plus']"
						class="mr-1"
					/>
					초대하기
				</button>
			</div>
		</div>

		<!-- 탭 -->
		<div class="h-[58px] px-6 flex items-end gap-10">
			<button
				class="h-full px-3 border-b-[3px] border-teal-500 text-teal-500 font-semibold"
			>
				<font-awesome-icon :icon="['far', 'calendar']" class="mr-2" />
				일정
			</button>

			<button
				class="h-full px-3 border-b-[3px] border-transparent text-gray-500 hover:text-teal-500"
			>
				<font-awesome-icon
					:icon="['far', 'note-sticky']"
					class="mr-2"
				/>
				전체 메모
			</button>

			<button
				class="h-full px-3 border-b-[3px] border-transparent text-gray-500 hover:text-teal-500"
			>
				<font-awesome-icon :icon="['far', 'message']" class="mr-2" />
				일별 메모
			</button>
		</div>
	</div>
</template>

<script setup lang="ts">
import type { PlanInfo } from "../types/PlanInfo";
import type { ConnectedUser } from "../types/ConnectedUser";
import { getImageSrc } from "@/utils/ImageUtil.ts";

defineProps<{
	planInfo?: PlanInfo;
	connectedUsers: ConnectedUser[];
}>();
</script>

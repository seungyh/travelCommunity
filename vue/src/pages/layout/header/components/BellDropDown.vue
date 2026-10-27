<template>
	<div
		class="btn hover:bg-gray-100 w-[40px] mr-[15px]! text-center content-center relative"
		@click.stop
	>
		<font-awesome-icon
			class="w-[20px] h-[20px]"
			:icon="['far', 'bell']"
			size="xl"
		/>

		<!-- 읽지 않은 알림 개수 -->
		<span
			v-if="totalCount > 0"
			class="absolute top-[2px] right-[1px] min-w-[17px] h-[17px] px-[4px] flex items-center justify-center rounded-full bg-red-500 text-white text-[10px] font-bold leading-none border-2 border-white"
		>
			{{ totalCount > 99 ? "99+" : totalCount }}
		</span>
		<Transition>
			<!-- 알림 드롭다운 -->
			<div
				v-if="openDropdownType == DropdownType.BELL"
				class="absolute z-50 mt-3 right-[-60px] w-[360px] max-h-[600px] overflow-hidden rounded-xl border border-gray-200 bg-white shadow-lg"
			>
				<!-- 헤더 -->
				<div
					class="flex items-center justify-between px-5 py-4 border-b border-gray-100"
				>
					<div class="flex items-center gap-2">
						<span class="text-[16px] font-bold text-gray-900">
							알림
						</span>

						<span
							v-if="totalCount > 0"
							class="flex items-center justify-center min-w-[20px] h-[20px] px-1.5 rounded-full bg-teal-500 text-[11px] font-semibold text-white"
						>
							{{ totalCount }}
						</span>
					</div>

					<button
						v-if="totalCount > 0"
						class="text-[12px] text-gray-400 hover:text-teal-600"
						@click="readAllAlarm"
					>
						모두 읽음
					</button>
				</div>

				<!-- 알림 목록 -->
				<div class="max-h-[400px] overflow-y-auto">
					<div
						v-for="alarm in alarmList"
						:key="alarm.id"
						class="relative flex gap-3 px-5 py-4 cursor-pointer border-b border-gray-100 hover:bg-gray-50"
						:class="{ 'bg-teal-50/50': !alarm.read }"
						@click="clickAlarm(alarm)"
					>
						<!-- 알림 타입별 아이콘 -->
						<div
							class="flex items-center justify-center shrink-0 w-10 h-10 rounded-full"
							:class="{
								'bg-blue-50 text-blue-500':
									alarm.alarmType === AlarmType.CHAT,
								'bg-teal-50 text-teal-500':
									alarm.alarmType === AlarmType.PLAN,
								'bg-gray-100 text-gray-500':
									alarm.alarmType === AlarmType.FOLLOW,
							}"
						>
							<!-- 채팅 -->
							<font-awesome-icon
								v-if="alarm.alarmType === AlarmType.CHAT"
								:icon="['far', 'comment-dots']"
							/>

							<!-- 여행 계획 초대 -->
							<font-awesome-icon
								v-else-if="alarm.alarmType === AlarmType.PLAN"
								:icon="['fas', 'plane']"
							/>

							<!-- 팔로우 -->
							<font-awesome-icon
								v-else-if="alarm.alarmType === AlarmType.FOLLOW"
								:icon="['fas', 'user-plus']"
							/>
						</div>

						<!-- 알림 내용 -->
						<div class="min-w-0 flex-1 pr-4">
							<p
								@click.stop="clickAlarm(alarm)"
								class="text-[13px] leading-5 text-gray-700"
							>
								<span
									@click.stop="moveToUserPage(alarm)"
									class="font-semibold text-gray-900 hover:underline"
								>
									{{ alarm.nickName }}
								</span>

								<template
									v-if="alarm.alarmType === AlarmType.CHAT"
								>
									님이 메시지를 보냈습니다.
								</template>

								<template
									v-else-if="
										alarm.alarmType === AlarmType.PLAN
									"
								>
									님이 여행 계획에 초대했습니다.
								</template>

								<template
									v-else-if="
										alarm.alarmType === AlarmType.FOLLOW
									"
								>
									님이 회원님을 팔로우했습니다.
								</template>
							</p>

							<!-- 채팅 메시지 -->
							<p
								v-if="alarm.alarmType === AlarmType.CHAT"
								class="mt-1 truncate text-[12px] text-gray-400"
							>
								{{ alarm.description }}
							</p>

							<!-- 여행 계획 제목 -->
							<p
								v-else-if="alarm.alarmType === AlarmType.PLAN"
								class="mt-1 text-[12px] font-medium text-gray-500"
							>
								{{ alarm.description }}
							</p>

							<!-- 생성 시간 -->
							<p
								class="mt-2 text-[11px] text-gray-400 text-right"
							>
								{{ getDiffDateFromToday(alarm.createdAt) }}
							</p>
						</div>

						<!-- 안 읽음 표시 -->
						<span
							v-if="!alarm.read"
							class="absolute right-4 top-5 w-2 h-2 rounded-full bg-teal-500"
						/>
					</div>

					<!-- 알림 없음 -->
					<div
						v-if="alarmList.length === 0"
						class="flex flex-col items-center justify-center py-12 text-gray-400"
					>
						<font-awesome-icon
							:icon="['far', 'bell']"
							class="text-[28px] mb-3"
						/>
						<p class="text-[13px]">새로운 알림이 없습니다.</p>
					</div>
				</div>

				<!-- 하단 -->
				<button
					class="w-full py-3 border-t border-gray-100 text-[12px] font-medium text-gray-500 hover:bg-gray-50 hover:text-teal-600"
					@click="getAllAlarm"
				>
					모든 알림 보기
				</button>
			</div>
		</Transition>
		<InviteAcceptModal
			v-if="isInviteModal"
			:plan-id="selectedAlarm?.planId"
			:nick-name="selectedAlarm?.nickName"
			:alarm-id="selectedAlarm?.id"
			@close="closeInviteModal"
		></InviteAcceptModal>
	</div>
</template>
<script setup lang="ts">
import { useAuthStore } from "@/stores/Auth";
import { DropdownType } from "../enmus/DropdownType";
import { onBeforeUnmount, onMounted, ref, watch } from "vue";
import type { AlarmInfo } from "../types/AlarmInfo";
import axios, { AxiosError, type AxiosResponse } from "axios";
import type { AlarmListResponse } from "../types/response/AlarmListResponse";
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";
import { AlarmType } from "../enmus/AlarmType";
import { getDiffDateFromToday } from "@/utils/DateUtil";
import InviteAcceptModal from "../modals/InviteAcceptModal.vue";
import { useRouter } from "vue-router";

const props = defineProps(["openDropdownType"]);

const auth = useAuthStore();
const router = useRouter();
let eventSource: EventSource | null = null;

const totalCount = ref<number>(0);
const alarmList = ref<AlarmInfo[]>([]);
const isInviteModal = ref<boolean>(false);
const selectedAlarm = ref<AlarmInfo | null>(null);

// 알림 클릭
const clickAlarm = (alarm: AlarmInfo) => {
	switch (alarm.alarmType) {
		case AlarmType.CHAT:
			// 채팅 화면 이동
			break;

		case AlarmType.PLAN:
			// 여행 계획 초대 수락 모달 출력
			selectedAlarm.value = alarm;
			isInviteModal.value = true;
			break;

		case AlarmType.FOLLOW:
			// 사용자 프로필 이동
			// router.push(`/profile/${alarm.senderId}`);
			break;
	}

	// TODO: 읽지 않은 알림이면 서버에 읽음 처리 요청
};

// 모두 읽음
const closeInviteModal = (isAccept: boolean, planId: number | null) => {
	isInviteModal.value = false;
	if (isAccept) {
		if (selectedAlarm.value) {
			// 화면에서 읽음으로 보이게 변경
			selectedAlarm.value.read = true;
		}
		if (confirm("해당 여행 계획 방으로 이동하시겠습니까?")) {
			router.push(`/travel/plan/${planId}`);
		}
		getAlarmCount();
	}
};

// 클릭한 사용자 페이지로 이동
const moveToUserPage = (alarm: AlarmInfo) => {
	router.push(`/my-page/${alarm.senderId}`);
	if (alarm.alarmType !== AlarmType.PLAN) {
		axios
			.post(`/web/api/alarm/read/${alarm.id}`)
			.then((res: AxiosResponse<number>) => {
				alarm.read = true;
				totalCount.value = res.data;
			})
			.catch((error: AxiosError<ErrorResponse>) => {
				console.log(error);
				alert(error.response?.data.errorMessage);
			});
	}
};
// 모든 알림 보기
const readAllAlarm = () => {
	// TODO: 여행 계획 초대를 제외한 모든 알림 읽음 처리
	alert("개발중입니당");
};

// 모든 안읽은 알람 가져오기
const getAllAlarm = () => {
	alert("개발중입니당");
};

const getAlarmList = () => {
	axios
		.get("/web/api/alarm")
		.then((res: AxiosResponse<AlarmListResponse>) => {
			alarmList.value = res.data.alarmList;
			sseConnect();
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			console.log(error);
			alert(error.response?.data.errorMessage);
		});
};
// 안읽은 알람 카운트 조회
const getAlarmCount = () => {
	axios
		.get("/web/api/alarm/count")
		.then((res: AxiosResponse<number>) => {
			totalCount.value = res.data;
			sseConnect();
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			console.log(error);
			alert(error.response?.data.errorMessage);
		});
};

// 알람 sse 구독
const sseConnect = () => {
	eventSource = new EventSource("/web/api/alarm/sse/subscribe");

	eventSource.onopen = () => {
		console.log("open sse");
	};
	eventSource.onmessage = (event) => {
		alarmList.value.push(event.data);
	};
};
// 연결 종료 함수
const disConnect = () => {
	if (eventSource) {
		eventSource.close();
	}
};

watch(
	() => auth.getLogin(),
	() => {
		if (auth.getLogin()) {
			getAlarmList();
			getAlarmCount();
		} else {
			disConnect();
		}
	},
);
onMounted(() => {
	if (auth.getLogin()) {
		getAlarmList();
		getAlarmCount();
	}
});
onBeforeUnmount(() => {
	if (eventSource) {
		disConnect();
	}
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

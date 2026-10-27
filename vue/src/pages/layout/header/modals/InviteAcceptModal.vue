<!-- ProfileImageModal.vue -->
<template>
	<!-- 배경 -->
	<Modal
		:close-button="true"
		:modal-class="'w-[460px]'"
		@close="emit('close', false, null)"
	>
		<template #header>여행 계획 초대</template>
		<template #body>
			<div>
				<div>
					<!-- 아이콘 및 제목 -->
					<div class="flex flex-col items-center">
						<div
							class="mb-4 flex h-16 w-16 items-center justify-center rounded-full bg-teal-50 text-teal-500"
						>
							<font-awesome-icon
								:icon="['fas', 'plane']"
								class="text-[26px]"
							/>
						</div>

						<h2 class="text-[21px] font-bold text-gray-900">
							여행 계획 초대
						</h2>

						<p
							class="mt-3 text-center text-[14px] leading-6 text-gray-600"
						>
							<span class="font-semibold text-gray-900">
								{{ nickName }}
							</span>
							님이 여행 계획에 초대했습니다.
						</p>
					</div>

					<!-- 여행 계획 정보 -->
					<div
						class="mt-7 rounded-xl border border-gray-200 bg-gray-50/50 p-5"
					>
						<h3
							class="mb-4 text-[16px] font-semibold text-gray-900"
						>
							{{ inviteInfo.title }}
						</h3>

						<div
							class="flex flex-col gap-3 text-[13px] text-gray-500"
						>
							<div class="flex items-center gap-3">
								<font-awesome-icon
									:icon="['fas', 'location-dot']"
									class="w-4"
								/>
								<span>{{ inviteInfo.place }}</span>
							</div>

							<div class="flex items-center gap-3">
								<font-awesome-icon
									:icon="['far', 'calendar']"
									class="w-4"
								/>
								<span>
									{{ inviteInfo.startAt }}
									~
									{{ inviteInfo.endAt }}
								</span>
							</div>
						</div>
					</div>

					<!-- 버튼 -->
					<div
						v-if="!inviteInfo.alreadyAccept"
						class="mt-7 grid grid-cols-2 gap-3"
					>
						<button
							class="h-[44px] rounded-lg border border-gray-300 bg-white text-[14px] font-semibold text-gray-600 transition-colors hover:bg-gray-50 cursor-pointer"
							@click="answerInvite(false)"
						>
							거절
						</button>

						<button
							class="h-[44px] rounded-lg bg-teal-500 text-[14px] font-semibold text-white transition-colors hover:bg-teal-600 cursor-pointer"
							@click="answerInvite(true)"
						>
							초대 수락
						</button>
					</div>
				</div>
			</div>
		</template>
	</Modal>
</template>
<script setup lang="ts">
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";
import Modal from "@/components/ModalComponent.vue";
import { useSpinnerStore } from "@/stores/Spinner";
import type { AxiosError, AxiosResponse } from "axios";
import axios from "axios";
import { onMounted, ref } from "vue";
import type { PlainInviteInfoResponse } from "../types/response/PlanInviteInfoResponse";
import type { PlanInviteAnswerRequest } from "../types/request/PlanInviteAnswerRequest";
import type { PlanInviteAlarmRequest } from "../types/request/PlanInviteAlarmRequest";

const { startSpinner, endSpinner } = useSpinnerStore();
const props = defineProps(["planId", "nickName", "alarmId"]);
const emit = defineEmits<{
	(e: "close", res: boolean, planId: number | null): void;
}>();

const inviteInfo = ref<PlainInviteInfoResponse>({
	title: "",
	place: "",
	startAt: null,
	endAt: null,
	alreadyAccept: false,
});

// 초대 수락 or 거절
const answerInvite = (answer: boolean) => {
	startSpinner();
	const request: PlanInviteAnswerRequest = {
		planId: props.planId,
		answer,
		alarmId: props.alarmId,
	};
	axios
		.post("/web/api/alarm/plan/invite", request)
		.then(() => {
			emit("close", true, props.planId);
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			alert(error.response?.data.errorMessage);
			console.log(error);
		})
		.finally(() => {
			endSpinner();
		});
};

// 초대 정보 가져오기
const getPlanInviteInfo = () => {
	startSpinner();
	const request: PlanInviteAlarmRequest = {
		alarmId: props.alarmId,
		planId: props.planId,
	};
	axios
		.get("/web/api/alarm/plan/invite", {
			params: request,
		})
		.then((res: AxiosResponse<PlainInviteInfoResponse>) => {
			inviteInfo.value = res.data;
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			alert(error.response?.data.errorMessage);
			console.log(error);
		})
		.finally(() => {
			endSpinner();
		});
};
onMounted(() => {
	getPlanInviteInfo();
});
</script>

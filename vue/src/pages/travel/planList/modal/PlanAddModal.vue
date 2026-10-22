<template>
	<Modal
		:close-button="true"
		:modal-class="'w-[600px]'"
		@close="emit('close')"
	>
		<template #header>새 여행 계획 만들기</template>
		<template #subTitle
			>특별한 여행을 계획하고, 소중한 사람들과 떠나보세요.</template
		>
		<template #body>
			<div>
				<div class="h-[40px] mb-8">
					<div class="flex flex-row">
						<div class="w-[25%] content-center required">
							여행 계획 제목
						</div>
						<div class="w-full relative">
							<input
								v-model="planCreateRequest.title"
								maxlength="30"
								type="text"
								placeholder="ex) 일본 4박 5일 힐링 여행"
								class="hover-green h-[40px] relative border-gray rounded-md p-1 pt-1 pb-1 custom-input w-full"
							/>
						</div>
					</div>
					<div class="text-right">
						<span class="text-gray-400"
							>{{ planCreateRequest.title.length }}/30</span
						>
					</div>
				</div>
				<div class="flex flex-row justify-between mt-3 h-[40px]">
					<div class="w-[25%] content-center required">여행 기간</div>
					<div
						class="hover-green relative border-gray rounded-md p-1 pt-1.5 pl-6 w-[48%]"
					>
						<flat-pickr
							v-model="planCreateRequest.startAt"
							:config="config"
							class="form-control w-[96%] cursor-pointer"
							placeholder="시작일 선택"
							name="date"
							autocomplete="off"
						/>
						<span
							class="absolute text-gray-500 -translate-y-1/2 pointer-events-none left-1 top-1/2 dark:text-gray-400"
						>
							<i class="bi bi-calendar4"></i>
						</span>
					</div>
					<span class="content-center">~</span>
					<div
						class="hover-green relative border-gray rounded-md p-1 pt-1.5 pl-6 w-[48%]"
					>
						<flat-pickr
							v-model="planCreateRequest.endAt"
							:config="config"
							class="form-control w-[96%] cursor-pointer"
							placeholder="종료일 선택"
							name="date"
							autocomplete="off"
						/>
						<span
							class="absolute text-gray-500 -translate-y-1/2 pointer-events-none left-1 top-1/2 dark:text-gray-400"
						>
							<i class="bi bi-calendar4"></i>
						</span>
					</div>
				</div>
				<div class="flex flex-row h-[40px] mt-3">
					<div class="w-[25%] content-center required">여행지</div>
					<div class="w-full relative">
						<input
							v-model="planCreateRequest.place"
							type="text"
							placeholder="ex) 도쿄, 오사카, 후쿠오카"
							class="hover-green h-[40px] relative border-gray rounded-md p-1 pt-1 pb-1 custom-input pl-6 w-full"
						/>
						<span
							class="absolute text-gray-500 -translate-y-1/2 pointer-events-none left-1 top-5 dark:text-gray-400"
						>
							<i class="bi bi-geo-alt-fill"></i>
						</span>
					</div>
				</div>
			</div>
			<div class="flex flex-row mt-3 float-end">
				<div
					@click="emit('close')"
					class="border-1 border-gray-300 w-[90px] h-[40px] content-center p-2 text-center rounded-lg mr-1 cursor-pointer"
				>
					취소
				</div>
				<div
					@click="addPlan"
					class="bg-teal-500 text-white w-[180px] h-[40px] content-center p-2 text-center rounded-lg cursor-pointer"
				>
					새 여행 계획 만들기
				</div>
			</div>
		</template>
	</Modal>
</template>
<script setup lang="ts">
import Modal from "@/components/ModalComponent.vue";
import { ref } from "vue";
import { Korean } from "flatpickr/dist/l10n/ko.js";
import flatPickr from "vue-flatpickr-component";
import type { AddNewPlanRequest } from "../types/request/AddNewPlanRequest";
import axios, { AxiosError, type AxiosResponse } from "axios";
import type { CommonResponse } from "@/components/common/types/response/CommonResponse";
import { useRouter } from "vue-router";
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";

const router = useRouter();

const emit = defineEmits(["close"]);

const planCreateRequest = ref<AddNewPlanRequest>({
	title: "",
	startAt: null,
	endAt: null,
	place: "",
});

const config = ref({
	dateFormat: "Y-m-d",
	wrap: true,
	clickOpens: true,
	allowInput: true,
	disableMobile: true,
	static: false,
	appendTo: document.body,
	positionElement: undefined,
	locale: Korean,
});
const validCheck = () => {
	if (!planCreateRequest.value.title) {
		alert("제목을 입력해주세요.");
		return false;
	}
	if (!planCreateRequest.value.place) {
		alert("여행 장소를 입력해주세요.");
		return false;
	}
	if (!planCreateRequest.value.startAt || !planCreateRequest.value.endAt) {
		alert("여행 날짜를 입력해주세요.");
		return false;
	}
	if (planCreateRequest.value.startAt > planCreateRequest.value.endAt) {
		alert("올바른 여행 날짜를 입력해주세요.");
		return false;
	}
	return true;
};
// 새 여행 계획 생성
const addPlan = () => {
	if (!validCheck()) {
		return;
	}
	axios
		.post("/web/api/travel", planCreateRequest.value)
		.then((res: AxiosResponse<CommonResponse>) => {
			if (res.data.result) {
				router.push(`/travel/plan/${res.data.message}`);
			}
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			alert(error.response?.data.errorMessage);
		});
};
</script>

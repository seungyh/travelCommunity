<template>
	<Modal
		:close-button="true"
		:modal-class="'w-[600px]'"
		@close="emit('close', false)"
	>
		<template #header>여행 정보 수정</template>
		<template #subTitle>여행 정보가 변경되나요?</template>
		<template #body>
			<div>
				<!-- 여행 제목 -->
				<div class="h-[40px] mb-8">
					<div class="flex flex-row">
						<div class="w-[25%] content-center required">
							여행 제목
						</div>

						<div class="w-full relative">
							<input
								v-model="travelInfoRequest.title"
								type="text"
								maxlength="30"
								placeholder="ex) 일본 4박 5일 힐링 여행"
								autocomplete="off"
								class="hover-green h-[40px] border-gray rounded-md p-1 custom-input w-full"
							/>
						</div>
					</div>

					<div class="text-right">
						<span class="text-gray-400 text-sm">
							{{ travelInfoRequest.title.length }}/30
						</span>
					</div>
				</div>

				<!-- 여행 기간 -->
				<div class="flex flex-row justify-between mt-3 h-[40px]">
					<div class="w-[25%] content-center required">여행 기간</div>

					<div
						class="hover-green relative border-gray rounded-md p-1 pt-1.5 pl-6 w-[48%]"
					>
						<flat-pickr
							v-model="travelInfoRequest.startAt"
							:config="config"
							class="form-control w-[96%] cursor-pointer"
							placeholder="시작일 선택"
							name="startDate"
							autocomplete="off"
						/>

						<span
							class="absolute text-gray-500 -translate-y-1/2 pointer-events-none left-1 top-1/2"
						>
							<i class="bi bi-calendar4"></i>
						</span>
					</div>

					<span class="content-center">~</span>

					<div
						class="hover-green relative border-gray rounded-md p-1 pt-1.5 pl-6 w-[48%]"
					>
						<flat-pickr
							v-model="travelInfoRequest.endAt"
							:config="config"
							class="form-control w-[96%] cursor-pointer"
							placeholder="종료일 선택"
							name="endDate"
							autocomplete="off"
						/>

						<span
							class="absolute text-gray-500 -translate-y-1/2 pointer-events-none left-1 top-1/2"
						>
							<i class="bi bi-calendar4"></i>
						</span>
					</div>
				</div>

				<!-- 여행지 -->
				<div class="flex flex-row h-[40px] mt-3">
					<div class="w-[25%] content-center required">여행지</div>

					<div class="w-full relative">
						<input
							v-model="travelInfoRequest.place"
							type="text"
							placeholder="ex) 도쿄, 오사카, 후쿠오카"
							autocomplete="off"
							class="hover-green h-[40px] border-gray rounded-md custom-input pl-6 pr-3 w-full"
						/>

						<span
							class="absolute text-gray-500 -translate-y-1/2 pointer-events-none left-2 top-1/2"
						>
							<i class="bi bi-geo-alt-fill"></i>
						</span>
					</div>
				</div>

				<!-- 여행 소개 -->
				<div class="flex flex-row mt-3">
					<div class="w-[25%] pt-2">여행 소개</div>

					<div class="w-full">
						<textarea
							v-model="travelInfoRequest.memo"
							maxlength="200"
							placeholder="이번 여행에 대해 간단히 작성해주세요."
							class="hover-green border-gray rounded-md custom-input resize-none p-2 w-full h-[90px]"
						></textarea>

						<div class="text-right">
							<span class="text-gray-400 text-sm">
								{{ travelInfoRequest.memo.length }}/200
							</span>
						</div>
					</div>
				</div>
			</div>

			<!-- 버튼 -->
			<div class="flex flex-row mt-5 float-end">
				<div
					@click="emit('close', false)"
					class="border border-gray-300 w-[90px] h-[40px] content-center p-2 text-center rounded-lg mr-1 cursor-pointer"
				>
					취소
				</div>

				<div
					@click="updateTravelInfo"
					class="bg-teal-500 text-white w-[120px] h-[40px] content-center p-2 text-center rounded-lg cursor-pointer"
				>
					수정하기
				</div>
			</div>
		</template>
	</Modal>
</template>
<script setup lang="ts">
import Modal from "@/components/ModalComponent.vue";
import { ref, watch } from "vue";
import axios, { AxiosError } from "axios";
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";
import { useRoute } from "vue-router";
import { useSpinnerStore } from "@/stores/Spinner";
import type { TravelInfoUpdateRequest } from "../types/request/TravelInfoUpdateRequest";
import { Korean } from "flatpickr/dist/l10n/ko.js";
import flatPickr from "vue-flatpickr-component";
import { dateValidCheck } from "@/utils/DateUtil";
import type { PlanInfo } from "../types/PlanInfo";

const route = useRoute();
const { startSpinner, endSpinner } = useSpinnerStore();

const props = defineProps<{
	planInfo?: PlanInfo;
}>();
const emit = defineEmits<{ (e: "close", value: boolean): void }>();

const config = ref({
	dateFormat: "Y-m-d",
	altFormat: "Y년 m월 d일 l",
	altInput: true,
	wrap: true,
	clickOpens: true,
	allowInput: true,
	disableMobile: true,
	static: false,
	appendTo: document.body,
	locale: Korean,
});
const travelInfoRequest = ref<TravelInfoUpdateRequest>({
	planId: 0,
	title: "",
	place: "",
	startAt: "",
	endAt: "",
	memo: "",
});

const validCheck = () => {
	if (!travelInfoRequest.value.title) {
		alert("여행의 제목을 입력해주세요.");
		return false;
	}
	if (!travelInfoRequest.value.place) {
		alert("여행지를 입력해주세요.");
		return false;
	}
	if (!travelInfoRequest.value.startAt) {
		alert("여행의 제목을 입력해주세요.");
		return false;
	}
	if (!travelInfoRequest.value.endAt) {
		alert("여행의 제목을 입력해주세요.");
		return false;
	}
	if (
		!dateValidCheck(
			travelInfoRequest.value.startAt,
			travelInfoRequest.value.endAt,
		)
	) {
		alert("올바른 날짜를 입력해주세요.");
		return false;
	}

	return true;
};
// 새 여행 계획 생성
const updateTravelInfo = () => {
	if (!validCheck()) {
		return;
	}
	startSpinner();
	travelInfoRequest.value.planId = route.params.id as unknown as number;
	axios
		.put("/web/api/travel", travelInfoRequest.value)
		.then(() => {
			alert("여행 정보가 수정되었습니다.");
			emit("close", true);
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			alert(error.response?.data.errorMessage);
		})
		.finally(() => endSpinner());
};

watch(
	() => props.planInfo,
	(value) => {
		console.log("props ", props.planInfo);
		if (props.planInfo) {
			travelInfoRequest.value.title = props.planInfo?.title;
			travelInfoRequest.value.startAt = props.planInfo?.startAt;
			travelInfoRequest.value.endAt = props.planInfo?.endAt;
			travelInfoRequest.value.place = props.planInfo?.place;
			// travelInfoRequest.value.memo = props.planInfo?.memo; // TODO:  memo 추가하면 넣기
		}
	},
	{
		immediate: true,
	},
);
</script>

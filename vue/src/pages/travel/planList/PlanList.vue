<template>
	<div class="p-5 mb-4 flex justify-between">
		<div>
			<div class="text-3xl font-bold mb-3">나의 여행 계획</div>
			<div>계획하는 순간, 여행은 더 특별해집니다</div>
		</div>
		<div class="flex flex-row">
			<div>
				<input placeholder="나의 여행 계획을 검색해보세요." />
			</div>
			<div
				@click="isOpen = true"
				class="bg-teal-500 text-white w-[180px] h-[40px] content-center p-2 text-center rounded-lg cursor-pointer"
			>
				+ 새 여행 계획 만들기
			</div>
		</div>
	</div>
	<div class="flex flex-col gap-3 w-[90%] justify-self-center">
		<div
			v-for="plan in planList"
			:key="plan.planId"
			class="group flex items-center justify-between gap-4 rounded-xl border border-gray-200 px-6 py-5 cursor-pointer transition-all duration-200 hover:border-teal-300 hover:shadow-md"
			:class="isNewPlan(plan.createdAt) ? 'bg-teal-50' : 'bg-white'"
			@click="moveDetailPlan(plan.planId)"
		>
			<!-- 왼쪽 -->
			<div class="flex min-w-0 items-center gap-5">
				<!-- 아이콘 -->
				<div
					class="flex h-12 w-12 shrink-0 items-center justify-center rounded-xl bg-teal-50 text-teal-500"
				>
					<font-awesome-icon :icon="['fas', 'plane']" />
				</div>

				<!-- 여행 정보 -->
				<div class="min-w-0">
					<div class="flex items-center gap-2">
						<h3
							class="truncate text-[17px] font-semibold text-gray-800 group-hover:text-teal-600"
							:title="plan.title"
						>
							{{ plan.title }}
						</h3>

						<span
							v-if="isNewPlan(plan.createdAt)"
							class="shrink-0 rounded-full bg-teal-50 px-2.5 py-1 text-[11px] font-semibold text-teal-600"
						>
							NEW
						</span>
					</div>

					<!-- 여행지 / 여행 기간 -->
					<div
						class="mt-2 flex flex-wrap items-center gap-x-4 gap-y-1 text-[13px] text-gray-500"
					>
						<span class="flex items-center gap-1.5">
							<font-awesome-icon
								:icon="['fas', 'location-dot']"
							/>
							{{ plan.place }}
						</span>

						<span class="flex items-center gap-1.5">
							<font-awesome-icon :icon="['far', 'calendar']" />
							{{ plan.startAt }} ~ {{ plan.endAt }}
						</span>
					</div>

					<!-- 생성일 -->
					<div
						class="mt-2 flex items-center gap-2 text-[12px] text-gray-400"
					>
						<font-awesome-icon :icon="['far', 'clock']" />

						<span>
							{{ dayjs(plan.createdAt).format("YYYY-MM-DD") }}
						</span>

						<span>·</span>

						<span>
							{{ getDiffDateFromToday(plan.createdAt) }}
						</span>
					</div>
				</div>
			</div>

			<!-- 오른쪽 화살표 -->
			<font-awesome-icon
				:icon="['fas', 'chevron-right']"
				class="shrink-0 text-[14px] text-gray-300 transition-all duration-200 group-hover:translate-x-1 group-hover:text-teal-500"
			/>
		</div>
	</div>
	<PlanAddModal v-if="isOpen" @close="closeModal"></PlanAddModal>
</template>
<script setup lang="ts">
import { onMounted, ref } from "vue";
import PlanAddModal from "./modal/PlanAddModal.vue";
import { useSpinnerStore } from "@/stores/Spinner.ts";
import type { AxiosError, AxiosResponse } from "axios";
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse.ts";
import axios from "axios";
import type { GetPlanListResponse } from "./types/response/GetPlanListResponse.ts";
import type { PlanOverview } from "./types/PlanOverview.ts";
import dayjs from "dayjs";
import { getDiffDateFromToday } from "@/utils/DateUtil.ts";
import { useRouter } from "vue-router";

const { startSpinner, endSpinner } = useSpinnerStore();
const router = useRouter();

const isOpen = ref(false);
const planList = ref<PlanOverview[]>([]); // 여행 계획 목록

const moveDetailPlan = (planId: number) => {
	router.push("/travel/plan/" + planId);
};
// 생성 후 10분 이내인지 확인
const isNewPlan = (createdAt: Date): boolean => {
	const diff = Date.now() - new Date(createdAt).getTime();

	return diff >= 0 && diff < 10 * 60 * 1000;
};
const getPlanList = () => {
	startSpinner();
	axios
		.get("/web/api/travel")
		.then((res: AxiosResponse<GetPlanListResponse>) => {
			planList.value = res.data.planList;
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			console.log(error);
			alert(error.response?.data.errorMessage);
		})
		.finally(() => {
			endSpinner();
		});
};
const closeModal = () => {
	isOpen.value = false;
};

onMounted(() => {
	getPlanList();
});
</script>

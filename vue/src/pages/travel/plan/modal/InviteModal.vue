<template>
	<Modal
		:close-button="true"
		:modal-class="'w-[600px]'"
		@close="emit('close')"
	>
		<template #header>참가자 초대</template>
		<template #subTitle
			>사용자 태그로 여행에 함께할 사람을 초대하세요.</template
		>
		<template #body>
			<div>
				<div class="flex flex-row min-h-[40px]">
					<div class="w-[25%] content-center required pb-5">
						사용자 태그
					</div>

					<div class="w-full">
						<div class="relative">
							<span
								class="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 font-semibold"
							>
								#
							</span>

							<input
								v-model="inviteMemberRequest.tag"
								type="text"
								placeholder="K3K3KNH"
								autocomplete="off"
								class="hover-green h-[40px] border-gray rounded-md custom-input pl-7 pr-3 w-full"
							/>
						</div>

						<div class="mt-1 text-xs text-gray-400">
							초대할 사용자의 태그를 입력해주세요.
						</div>
					</div>
				</div>
			</div>

			<div class="flex flex-row mt-6 float-end">
				<div
					@click="emit('close')"
					class="border border-gray-300 w-[90px] h-[40px] content-center p-2 text-center rounded-lg mr-1 cursor-pointer"
				>
					취소
				</div>

				<div
					@click="inviteMember"
					class="bg-teal-500 text-white w-[120px] h-[40px] content-center p-2 text-center rounded-lg cursor-pointer"
				>
					<font-awesome-icon
						:icon="['fas', 'user-plus']"
						class="mr-1"
					/>
					초대하기
				</div>
			</div>
		</template>
	</Modal>
</template>
<script setup lang="ts">
import Modal from "@/components/ModalComponent.vue";
import { ref } from "vue";
import axios, { AxiosError } from "axios";
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";
import type { InviteMemberRequest } from "../types/request/InviteMemberRequest";
import { useRoute } from "vue-router";
import { useSpinnerStore } from "@/stores/Spinner";

const route = useRoute();
const { startSpinner, endSpinner } = useSpinnerStore();
const emit = defineEmits(["close"]);

const inviteMemberRequest = ref<InviteMemberRequest>({
	planId: 0,
	tag: "",
});

const validCheck = () => {
	if (!inviteMemberRequest.value.tag) {
		alert("초대할 사용자의 태그를 입력해주세요.");
		return false;
	}
	return true;
};
// 새 여행 계획 생성
const inviteMember = () => {
	if (!validCheck()) {
		return;
	}
	startSpinner();
	inviteMemberRequest.value.planId = route.params.id as unknown as number;
	axios
		.post("/web/api/travel/plan/invite", inviteMemberRequest.value)
		.then(() => {
			alert("사용자를 초대하였습니다.");
			emit("close");
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			alert(error.response?.data.errorMessage);
		})
		.finally(() => endSpinner());
};
</script>

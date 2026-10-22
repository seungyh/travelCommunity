<template>
	<Modal
		:close-button="true"
		:modal-class="'w-[600px]'"
		@close="emit('close', false)"
	>
		<template #header>프로필 편집</template>
		<template #body>
			<div>
				<div>
					<!-- 여행 제목 -->
					<div class="h-[40px] mb-8">
						<div class="flex flex-row">
							<div class="w-[25%] content-center required">
								닉네임
							</div>

							<div class="w-full relative">
								<input
									v-model="profileInfo.nickName"
									type="text"
									maxlength="20"
									placeholder="여행자"
									autocomplete="off"
									class="hover-green h-[40px] border-gray rounded-md p-1 custom-input w-full"
								/>
							</div>
						</div>

						<div class="text-right">
							<span class="text-gray-400 text-sm">
								{{ profileInfo.nickName.length }}/20
							</span>
						</div>
					</div>

					<!-- 자기 소개 -->
					<div class="flex flex-row mt-3">
						<div class="w-[25%] pt-2">자기 소개</div>

						<div class="w-full">
							<textarea
								v-model="profileInfo.bio"
								maxlength="100"
								placeholder="자기 소개를 간단히 작성해주세요."
								class="hover-green border-gray rounded-md custom-input resize-none p-2 w-full h-[90px]"
							></textarea>

							<div class="text-right">
								<span class="text-gray-400 text-sm">
									{{
										profileInfo.bio
											? profileInfo.bio.length
											: 0
									}}/100
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
						@click="saveProfile"
						class="bg-teal-500 text-white w-[120px] h-[40px] content-center p-2 text-center rounded-lg cursor-pointer"
					>
						저장
					</div>
				</div>
			</div>
		</template>
	</Modal>
</template>
<script setup lang="ts">
import Modal from "@/components/ModalComponent.vue";
import { onMounted, ref } from "vue";
import type {
	ProfileResponse,
	ProfileSaveRequest,
} from "../types/ProfileSaveInfo";
import { useSpinnerStore } from "@/stores/Spinner";
import axios, { AxiosError, type AxiosResponse } from "axios";
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";

const { startSpinner, endSpinner } = useSpinnerStore();

const emit = defineEmits<{ (e: "close", value: boolean): void }>();

const profileInfo = ref<ProfileSaveRequest>({
	nickName: "",
	bio: "",
	location: "",
});

// 프로필 정보 가져오기
const getProfile = () => {
	startSpinner();
	axios
		.get("/web/api/account/profile")
		.then((res: AxiosResponse<ProfileResponse>) => {
			profileInfo.value.bio = res.data.bio;
			profileInfo.value.nickName = res.data.nickName;
			profileInfo.value.location = res.data.location;
			console.log("profileInfo ", profileInfo.value);
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			alert(error.response?.data.errorMessage);
		})
		.finally(() => {
			endSpinner();
		});
};

const saveProfile = () => {
	startSpinner();
	axios
		.post("/web/api/account/profile", profileInfo.value)
		.then((res: AxiosResponse<ProfileResponse>) => {
			emit("close", true);
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			alert(error.response?.data.errorMessage);
		})
		.finally(() => {
			endSpinner();
		});
};
onMounted(() => {
	getProfile();
});
</script>

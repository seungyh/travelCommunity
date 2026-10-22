<!-- ProfileImageModal.vue -->
<template>
	<!-- 배경 -->
	<Modal
		:close-button="true"
		:modal-class="'w-[690px]'"
		@close="emit('close', false)"
	>
		<template #header>프로필 편집</template>
		<template #body>
			<div>
				<!-- Content -->
				<div class="px-8 py-7">
					<div class="flex gap-8">
						<!-- 이미지 -->
						<div
							class="relative flex h-[360px] w-[400px] items-center justify-center overflow-hidden rounded-lg bg-gray-100"
						>
							<img
								:src="getImageSrc(imageUrl)"
								class="h-full w-full object-cover"
							/>

							<!-- 원형 Crop 표시 -->
							<div
								class="absolute h-[360px] w-[382px] rounded-full border-2 border-white shadow-[0_0_0_500px_rgba(0,0,0,0.35)]"
							></div>
						</div>

						<!-- 오른쪽 -->
						<div class="flex flex-1 flex-col">
							<p class="mb-3 text-sm font-medium text-gray-500">
								미리보기
							</p>

							<div
								class="mx-auto h-[145px] w-[145px] overflow-hidden rounded-full"
							>
								<img
									:src="getImageSrc(imageUrl)"
									class="h-full w-full object-cover"
								/>
							</div>

							<label
								class="mt-7 flex h-[48px] cursor-pointer items-center justify-center rounded-lg border border-gray-300 text-sm font-semibold text-gray-700 hover:bg-gray-50"
							>
								<font-awesome-icon
									:icon="['fas', 'upload']"
									class="mr-2"
								/>

								이미지 업로드

								<input
									@change="setPreview"
									type="file"
									accept="image/jpeg,image/png,image/webp"
									class="hidden"
								/>
							</label>

							<p class="mt-2 text-center text-xs text-gray-400">
								JPG, PNG, WEBP (최대 5MB)
							</p>
						</div>
					</div>
				</div>

				<!-- Footer -->
				<div class="flex justify-end gap-3 px-8 pb-7">
					<button
						class="h-11 min-w-[120px] rounded-lg border border-gray-300 bg-white text-sm font-semibold text-gray-600 hover:bg-gray-50"
						@click="emit('close', false)"
					>
						취소
					</button>

					<button
						class="h-11 min-w-[120px] rounded-lg bg-teal-500 text-sm font-semibold text-white hover:bg-teal-600"
						@click="saveProfileImage"
					>
						변경하기
					</button>
				</div>
			</div>
		</template>
	</Modal>
</template>
<script setup lang="ts">
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";
import Modal from "@/components/ModalComponent.vue";
import { useSpinnerStore } from "@/stores/Spinner";
import { getImageSrc } from "@/utils/ImageUtil";
import type { AxiosError } from "axios";
import axios from "axios";
import { ref, watch } from "vue";

const { startSpinner, endSpinner } = useSpinnerStore();
const props = defineProps(["imagePath"]);
const emit = defineEmits<{ (e: "close", value: boolean): void }>();

const imageUrl = ref<string>("");
const file = ref<File>();

// 이미지 업로드 및 미리보기 url 생성
const setPreview = (event: Event) => {
	if (imageUrl.value) {
		URL.revokeObjectURL(imageUrl.value);
	}
	const target = event.target as HTMLInputElement;
	file.value = target.files?.[0];
	if (!file.value) {
		alert("유효하지 않은 파일입니다.");
		return;
	}
	if (file.value.size > 1024 * 1024 * 5) {
		alert("5MB 이하의 이미지만 업로드 가능합니다.");
		return;
	}
	imageUrl.value = URL.createObjectURL(file.value);
};

// 파일 저장
const saveProfileImage = () => {
	startSpinner();
	const formData = new FormData();
	if (file.value) {
		formData.append("file", file.value);
	}
	const jsonBlob = new Blob([JSON.stringify("PROFILE")], {
		type: "application/json",
	});
	formData.append("type", jsonBlob);
	// 파일 업로드
	startSpinner();
	axios
		.post("/web/api/account/profile/image", formData, {
			headers: { "Content-Type": "multipart/form-data" },
		})
		.then(() => {
			emit("close", true);
		})
		.catch((res: AxiosError<ErrorResponse>) => {
			alert("파일 업로드 실패하였습니다.");
		})
		.finally(() => {
			endSpinner();
		});
};
watch(
	() => props.imagePath,
	() => {
		imageUrl.value = props.imagePath;
	},
	{
		immediate: true,
	},
);
</script>

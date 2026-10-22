<template>
	<Modal
		:close-button="true"
		:modal-class="'w-[720px]'"
		@close="emit('close', false)"
	>
		<template #header>배경 이미지 변경</template>
		<template #body>
			<div>
				<div class="px-8 py-7">
					<div
						class="relative w-full h-[275px] overflow-hidden rounded-xl bg-gray-100"
					>
						<!-- 원본 이미지: 가로는 무조건 전부 표시 -->
						<img
							:src="
								imageUrl
									? getImageSrc(imageUrl)
									: '/public/구름.jpg'
							"
							class="w-full h-auto absolute left-0 top-1/2 -translate-y-1/2"
						/>

						<!-- 실제 배경으로 사용될 영역 -->
						<div
							class="absolute left-0 right-0 top-1/2 -translate-y-1/2 h-[110px] border-2 border-white shadow-[0_0_0_9999px_rgba(0,0,0,0.25)]"
						>
							<!-- 가이드 -->
							<div
								class="absolute left-1/3 top-0 h-full border-l border-white/50"
							/>

							<div
								class="absolute left-2/3 top-0 h-full border-l border-white/50"
							/>

							<div
								class="absolute left-0 top-1/2 w-full border-t border-white/50"
							/>
						</div>
					</div>

					<!-- 미리보기 -->
					<p class="mb-3 mt-5 text-sm font-medium text-gray-500">
						미리보기
					</p>

					<div
						class="h-[110px] overflow-hidden rounded-xl bg-gray-100"
					>
						<img
							:src="
								imageUrl
									? getImageSrc(imageUrl)
									: '/public/구름.jpg'
							"
							class="h-full w-full object-cover"
						/>
					</div>

					<!-- 이미지 메뉴 -->
					<div class="mt-5 flex gap-2">
						<label
							class="flex h-11 cursor-pointer items-center justify-center rounded-lg bg-teal-500 px-5 text-sm font-semibold text-white hover:bg-teal-600"
						>
							<font-awesome-icon
								:icon="['fas', 'upload']"
								class="mr-2"
							/>

							이미지 업로드

							<input
								type="file"
								@input="setPreview"
								accept="image/jpeg,image/png,image/webp"
								class="hidden"
							/>
						</label>

						<button
							@click="imageUrl = ''"
							class="h-11 rounded-lg border border-gray-300 px-5 text-sm font-medium text-gray-600 hover:bg-gray-50 cursor-pointer"
						>
							<font-awesome-icon
								:icon="['far', 'image']"
								class="mr-2"
							/>
							기본 이미지
						</button>
					</div>

					<!-- 설명 -->
					<div class="mt-4 text-xs leading-6 text-gray-400">
						<p>JPG, PNG, WEBP (최대 5MB)</p>
						<p>
							• 업로드한 이미지는 화면 크기에 맞게 잘라서
							표시됩니다.
						</p>
					</div>
				</div>

				<!-- Footer -->
				<div class="flex justify-end gap-3 px-8 pb-7">
					<button
						class="h-11 min-w-[120px] rounded-lg border border-gray-300 text-sm font-semibold text-gray-600 hover:bg-gray-50"
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
	const jsonBlob = new Blob([JSON.stringify("BACKGROUND")], {
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

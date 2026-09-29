<template>
	<div
		class="fixed inset-0 z-99999 flex items-center justify-center overflow-hidden"
	>
		<div
			v-if="fullScreenBackdrop"
			class="fixed inset-0 bg-black/30 backdrop-blur-sm"
			aria-hidden="true"
			@click="$emit('close')"
		></div>

		<div
			class="relative z-50 flex max-h-[90vh] flex-col overflow-hidden rounded-2xl bg-white shadow-xl"
			:class="modalClass"
		>
			<!-- Header -->
			<div
				class="sticky top-0 z-10 flex items-center justify-between bg-white pl-8 pr-8 pt-5 pb-2"
			>
				<div class="text-2xl font-semibold text-gray-800">
					<slot name="header"></slot>
				</div>

				<button
					v-if="closeButton"
					@click="$emit('close')"
					class="flex items-center cursor-pointer justify-center w-8 h-8 rounded-full bg-gray-100 text-gray-500 transition hover:bg-gray-200 hover:text-gray-700"
				>
					<font-awesome-icon
						:icon="['fas', 'xmark']"
						class="text-sm"
					/>
				</button>
			</div>

			<!-- sub title -->
			<div
				class="sticky top-0 z-10 flex items-center justify-between bg-white pl-8 pr-8 pb-5"
			>
				<div class="text-sm text-gray-500">
					<slot name="subTitle"></slot>
				</div>
			</div>

			<!-- Body -->
			<div class="overflow-y-auto px-8 pb-6">
				<slot name="body"></slot>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted } from "vue";

interface ModalProps {
	fullScreenBackdrop?: boolean;
	closeButton?: boolean;
	modalClass?: string;
}

withDefaults(defineProps<ModalProps>(), {
	fullScreenBackdrop: true,
	closeButton: true,
	modalClass: "w-[700px]",
});

defineEmits(["close"]);

// 모달이 열려 있는 동안 배경(body) 스크롤 잠금, 닫히면 복원
onMounted(() => {
	document.body.style.overflow = "hidden";
});

onUnmounted(() => {
	document.body.style.overflow = "";
});
</script>

import { defineStore } from "pinia";
import { ref } from "vue";

/**
 * 스피너 전역 관리
 */
export const useSpinnerStore = defineStore("spinner", () => {
	const isLoading = ref<boolean>(false);

	const startSpinner = () => {
		isLoading.value = true;
	};
	const endSpinner = () => {
		isLoading.value = false;
	};

	return {
		isLoading,
		startSpinner,
		endSpinner,
	};
});

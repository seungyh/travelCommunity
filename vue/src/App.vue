<template>
	<router-view class="relative"></router-view>
	<Spinner v-if="spinner.isLoading" />
</template>

<script setup lang="ts">
import Spinner from "./components/Spinner.vue";
import { useSpinnerStore } from "@/stores/Spinner.ts";

const spinner = useSpinnerStore();
</script>

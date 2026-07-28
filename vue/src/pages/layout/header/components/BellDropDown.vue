<template>
	<div
		class="btn hover:bg-gray-100 w-[25px] mr-[15px]! text-center content-center"
		@click.stop
	>
		<bell-icon class="w-full h-full" />
		<Transition>
			<div v-if="openDropdownType == DropdownType.BELL">
				open drop down menu
			</div>
		</Transition>
	</div>
</template>
<script setup lang="ts">
import BellIcon from "@/components/icons/BellIcon.vue";
import { DropdownType } from "../types/DropdownType";

const props = defineProps(["openDropdownType"]);
</script>
<style scoped>
.profile-img {
	border-radius: 25px;
	width: 50px;
	height: 50px;
	border: 1px solid black;
	cursor: pointer;
}
</style>

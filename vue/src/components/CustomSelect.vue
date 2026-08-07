<template>
	<div @click.stop>
		<div class="relative">
			<div
				v-if="!selectedItem"
				class="bg-white cursor-pointer"
				@click="isMenuOpen = true"
			>
				{{ placeholder }}
			</div>
			<div
				v-else
				class="bg-white cursor-pointer"
				@click="isMenuOpen = true"
			>
				{{ selectedItem.label }}
			</div>
			<span
				class="absolute z-30 text-gray-400 -translate-y-1/2 pointer-events-none right-0 top-2/5 text-xs"
			>
				<font-awesome-icon :icon="['fas', 'chevron-down']" />
			</span>
			<Transition>
				<div v-if="isMenuOpen" class="dropdown-menu left-0">
					<ul>
						<li
							v-for="(item, index) in items"
							@click="select(index)"
						>
							{{ item.label }}
						</li>
					</ul>
				</div>
			</Transition>
		</div>
	</div>
</template>
<script setup lang="ts">
import { onMounted, ref } from "vue";
import type { SelectItem } from "./common/types/SelectItem";

const props = defineProps(["placeholder", "item", "items"]);
const emit = defineEmits<{
	(e: "change", item: SelectItem): void;
}>();

const isMenuOpen = ref(false);
const selectedItem = ref<SelectItem>({
	label: "",
	value: "",
});

const select = (index: number | string) => {
	selectedItem.value = props.items[index]; // 결과 저장
	isMenuOpen.value = false; // 메뉴창 닫음
	emit("change", selectedItem.value);
};
// 바탕 클릭 시 드롭다운 닫음
const handleBodyClick = () => {
	isMenuOpen.value = false;
};

onMounted(() => {
	// 부모에서 default value 저장
	selectedItem.value = props.item;
	window.addEventListener("click", handleBodyClick);
});
</script>
<style scoped>
.dropdown-menu ul li {
	width: 100%;
	height: 40px;
	padding-left: 10px;
	padding-right: 10px;
	cursor: pointer;
	align-content: center;
}
.dropdown-menu ul li:hover {
	background-color: #f4f5f8;
}
</style>

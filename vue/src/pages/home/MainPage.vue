<template>
	<div class="p-5 mb-4">
		<div class="text-3xl font-bold mb-3">인기 여행 후기</div>
		<div>전 세계 여행자들의 생생한 여행 이야기를 만나보세요</div>
	</div>
	<div class="flex justify-between mr-8">
		<div
			class="w-[300px] rounded-3xl bg-gray-200 flex justify-around h-[50px] ml-4"
		>
			<div
				class="w-[48%] h-[40px] self-center content-center ml-2 text-center rounded-3xl cursor-pointer"
				:class="
					sortField !== 'like_count'
						? 'text-gray-400'
						: 'bg-teal-500 text-white'
				"
				@click="changeSort('like_count')"
			>
				최근 인기순
			</div>
			<div
				class="w-[48%] h-[40px] self-center content-center mr-2 text-center rounded-3xl cursor-pointer"
				:class="
					sortField !== 'created_at'
						? 'text-gray-400'
						: 'bg-teal-500 text-white'
				"
				@click="changeSort('created_at')"
			>
				최신순
			</div>
		</div>
		<div>
			총 <span class="font-bold">{{ totalCount }}</span
			>개의 게시글
		</div>
	</div>
	<BoardList
		ref="boardListRef"
		:sortField="sortField"
		:filter="filter"
		@totalCount="(count) => (totalCount = count)"
	/>
</template>
<script setup lang="ts">
import { onMounted, ref } from "vue";
import BoardList from "../board/search/BoardList.vue";
import type { BoardSearchFilter } from "../board/search/types/BoardSearchFilter.ts";
import { hasCookie } from "@/utils/CookieUtil.ts";
import axios from "axios";

const boardListRef = ref<InstanceType<typeof BoardList> | null>(null);
// 검색 조건 최근 인기순, 최신순
const sortField = ref<"like_count" | "created_at">("like_count");
const totalCount = ref(0);
const filter = ref<BoardSearchFilter>({
	titleOrContent: null,
	place: null,
	nickName: null,
	tag: [],
	category: null,
	isNext: true,
	dateCursor: new Date(),
	likeCursor: 0,
});

const changeSort = (field: "like_count" | "created_at") => {
	console.log("field ", field);
	// 현재 정렬중인 필드면 처리 안함
	if (sortField.value === field) {
		return;
	}
	sortField.value = field;
	getBoardList(field);
};
const getBoardList = (field: string) => {
	if (boardListRef.value) {
		boardListRef.value.getNextBoardList(field);
	}
};
onMounted(async () => {
	if (!hasCookie("XSRF-TOKEN=")) {
		await axios.get("/web/api/account/csrf");
	}
	getBoardList("like_count");
});
</script>

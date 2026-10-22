<!-- TODO: 개발 필요 -->
<template>
	<!-- 게시글 -->
	<div class="max-w-[980px] mx-auto pt-8">
		<!-- 필터 -->
		<div class="flex items-center gap-2 mb-6">
			<button
				class="h-8 px-4 rounded-full bg-teal-500 text-white text-sm font-semibold"
			>
				전체
			</button>

			<button
				class="h-8 px-4 rounded-full bg-white border border-gray-200 text-gray-600 text-sm"
			>
				국내
			</button>

			<button
				class="h-8 px-4 rounded-full bg-white border border-gray-200 text-gray-600 text-sm"
			>
				해외
			</button>

			<span class="ml-1 text-sm text-gray-400"> 6개 </span>
		</div>

		<!-- 카드 -->
		<div class="grid grid-cols-3 gap-5">
			<div
				v-for="post in posts"
				:key="post.id"
				class="relative h-[230px] rounded-xl overflow-hidden bg-white shadow-sm group"
			>
				<img
					:src="post.image"
					:alt="post.title"
					class="w-full h-full object-cover transition-transform duration-300 group-hover:scale-105"
				/>

				<!-- 어두운 그라데이션 -->
				<div
					class="absolute inset-0 bg-gradient-to-t from-black/50 via-transparent to-transparent"
				></div>

				<!-- 국내 -->
				<div
					class="absolute top-3 left-3 bg-white/90 text-gray-600 text-xs px-3 py-1 rounded-full"
				>
					{{ post.location }}
				</div>

				<!-- 수정 / 삭제 -->
				<div class="absolute top-3 right-3 flex gap-2">
					<button
						class="w-8 h-8 bg-white/95 rounded-full text-gray-500 shadow hover:text-teal-600"
					>
						<i class="fa-solid fa-pen text-xs"></i>
					</button>

					<button
						class="w-8 h-8 bg-white/95 rounded-full text-red-500 shadow hover:bg-red-50"
					>
						<i class="fa-regular fa-trash-can text-xs"></i>
					</button>
				</div>

				<div class="absolute left-4 bottom-4 text-white">
					<p class="font-semibold">
						{{ post.title }}
					</p>
				</div>
			</div>
		</div>
	</div>
</template>
<script setup lang="ts">
import type { SearchRequest } from "@/components/common/types/request/SearchRequest";
import type { BoardSearchFilter } from "@/pages/board/search/types/BoardSearchFilter";
import type { BoardSearchInfo } from "@/pages/board/search/types/BoardSearchInfo";
import type { BoardSearchResponse } from "@/pages/board/search/types/response/BoardSearchResponse";
import { useSpinnerStore } from "@/stores/Spinner";
import axios, { type AxiosResponse } from "axios";
import { ref } from "vue";

interface Post {
	id: number;
	title: string;
	location: string;
	image: string;
}

const posts: Post[] = [
	{
		id: 1,
		title: "제주도 여행",
		location: "국내",
		image: "https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff",
	},
	{
		id: 2,
		title: "부산 여행",
		location: "국내",
		image: "https://images.unsplash.com/photo-1507525428034-b723cf961d3e",
	},
	{
		id: 3,
		title: "서울 여행",
		location: "국내",
		image: "https://images.unsplash.com/photo-1538485399081-7c8974e1c8b1",
	},
];
const { startSpinner, endSpinner } = useSpinnerStore();

// 페이지 조회 공통 조건
const searchRequest = ref<SearchRequest<BoardSearchFilter>>({
	filter: null,
	sortField: "created_at",
	sortDirection: "ASC",
	page: 0,
	size: 9,
});

const boardList = ref<BoardSearchInfo[]>();

const getPreBoardList = () => {
	searchRequest.value.filter.isNext = false;
};
// 다음 게시글 조회
const getNextBoardList = (sortField: string) => {
	startSpinner();
	searchRequest.value.filter.isNext = true;
	const isChangeField = searchRequest.value.sortField !== sortField; // 정렬 필드가 바뀌면
	searchRequest.value.sortField = sortField;

	const lastBoard = boardList.value?.[boardList.value.length - 1]; // 마지막 게시글 가져오기

	// date cursor, likeCount cursor 세팅
	if (isChangeField) {
		searchRequest.value.filter.dateCursor = new Date();
		searchRequest.value.filter.likeCursor = 99999999;
	} else if (lastBoard) {
		searchRequest.value.filter.dateCursor = lastBoard?.createdAt;
		searchRequest.value.filter.likeCursor = lastBoard?.likeCount;
	}
	axios
		.post("/web/api/board/search", searchRequest.value)
		.then((res: AxiosResponse<BoardSearchResponse>) => {
			boardList.value = res.data.boardSearchInfo;
			// emit("totalCount", res.data.totalCount);
		})
		.catch()
		.finally(() => {
			endSpinner();
		});
};
</script>

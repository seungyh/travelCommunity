<template>
	<!-- 게시글 -->
	<div class="max-w-[980px] mx-auto pt-8">
		<!-- 카테고리 및 제목·내용 검색 -->
		<div class="flex flex-wrap items-center gap-3 mb-6">
			<div class="flex flex-wrap items-center gap-2">
				<button
					v-for="category in categories"
					:key="category.value ?? 'ALL'"
					type="button"
					:aria-pressed="selectedCategory === category.value"
					class="h-8 px-4 rounded-full text-sm border transition-colors cursor-pointer"
					:class="
						selectedCategory === category.value
							? 'bg-teal-500 border-teal-500 text-white font-semibold'
							: 'bg-white border-gray-200 text-gray-600 hover:border-teal-400 hover:text-teal-600'
					"
					@click="selectCategory(category.value)"
				>
					{{ category.label }}
				</button>
				<span class="ml-1 text-sm text-gray-400 whitespace-nowrap"
					>{{ totalCount }}개</span
				>
			</div>
			<form
				class="ml-auto flex w-full sm:w-auto items-center gap-2"
				role="search"
				@submit.prevent="searchBoards"
			>
				<label for="user-board-keyword" class="sr-only"
					>제목 또는 내용 검색</label
				>
				<input
					id="user-board-keyword"
					v-model="searchKeyword"
					type="search"
					placeholder="제목 또는 내용 검색"
					class="h-9 min-w-0 flex-1 sm:w-52 rounded-lg border border-gray-200 bg-white px-3 text-sm text-gray-700 outline-none focus:border-teal-500 focus:ring-2 focus:ring-teal-100"
				/>
				<button
					type="submit"
					class="h-9 shrink-0 rounded-lg bg-teal-500 px-4 text-sm font-semibold text-white hover:bg-teal-600 cursor-pointer"
				>
					검색
				</button>
			</form>
		</div>
		<p
			v-if="!isLoading && posts.length === 0"
			class="py-10 text-center text-gray-400"
		>
			조회 가능한 게시글이 없습니다.
		</p>
		<!-- 카드 -->
		<!-- <div class="grid grid-cols-3 gap-5">
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

				<div
					class="absolute inset-0 bg-gradient-to-t from-black/50 via-transparent to-transparent"
				></div>

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
		</div> -->
	</div>
</template>
<script setup lang="ts">
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";
import type { SearchRequest } from "@/components/common/types/request/SearchRequest";
import type {
	UserBoardCategory,
	UserBoardSearchFilter,
} from "../types/UserBoardSearchFilter";
import type { BoardSearchInfo } from "@/pages/board/search/types/BoardSearchInfo";
import type { BoardSearchResponse } from "@/pages/board/search/types/response/BoardSearchResponse";
import axios, { type AxiosError } from "axios";
import { computed, onBeforeUnmount, ref, watch } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();
const boardList = ref<BoardSearchInfo[]>([]);
const totalCount = ref(0);
const isLoading = ref(false);
let requestId = 0;
const selectedCategory = ref<UserBoardCategory | null>(null);
const searchKeyword = ref("");
const appliedKeyword = ref("");
const categories: { label: string; value: UserBoardCategory | null }[] = [
	{ label: "전체", value: null },
	{ label: "국내 여행", value: "DOMESTIC" },
	{ label: "해외 여행", value: "OVERSEAS" },
	{ label: "맛집탐방", value: "FOOD" },
	{ label: "숙소리뷰", value: "ACCOMMODATION" },
	{ label: "여행 팁", value: "TIP" },
];

const selectCategory = (category: UserBoardCategory | null) => {
	selectedCategory.value = category;
	getUserBoardList();
};
const searchBoards = () => {
	appliedKeyword.value = searchKeyword.value.trim();
	getUserBoardList();
};

const posts = computed(() =>
	boardList.value.map((board) => ({
		id: board.boardId,
		title: board.title,
		image: `/web/api/board/feature/${board.boardId}`,
	})),
);

const getUserBoardList = () => {
	const currentRequestId = ++requestId;
	boardList.value = [];
	totalCount.value = 0;
	isLoading.value = false;
	const userId = route.params.userId;
	if (typeof userId !== "string" || !userId) return;

	const searchRequest: SearchRequest<UserBoardSearchFilter> = {
		filter: {
			category: selectedCategory.value,
			titleOrContent: appliedKeyword.value || null,
		},
		sortField: "created_at",
		sortDirection: "DESC",
		page: 0,
		size: 9,
	};
	isLoading.value = true;
	return axios
		.post<BoardSearchResponse>(
			`/web/api/board/user/${userId}`,
			searchRequest,
		)
		.then((res) => {
			if (currentRequestId !== requestId) return;
			boardList.value = res.data.boardSearchInfo;
			totalCount.value = res.data.totalCount;
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			if (currentRequestId !== requestId) return;
			alert(
				error.response?.data.errorMessage ??
					"게시글 목록을 불러오지 못했습니다.",
			);
		})
		.finally(() => {
			if (currentRequestId === requestId) isLoading.value = false;
		});
};

watch(() => route.params.userId, getUserBoardList, { immediate: true });
onBeforeUnmount(() => {
	requestId++;
});
</script>

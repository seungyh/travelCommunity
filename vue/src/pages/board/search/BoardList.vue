<template>
	<div class="p-4">
		<div class="grid grid-cols-3 gap-4">
			<div
				v-for="(board, index) in boardList"
				class="group rounded-lg border-gray hover:shadow-md cursor-pointer"
				@click="router.push('/board/detail/' + board.boardId)"
			>
				<div class="h-[200px] overflow-hidden rounded-t-lg">
					<img
						:src="'/web/api/board/feature/' + board.boardId"
						class="object-cover transition-transform duration-300 group-hover:scale-105"
					/>
				</div>
				<div class="py-2 px-4">
					<div
						class="text-lg font-bold whitespace-nowrap overflow-hidden text-ellipsis"
					>
						{{ board.title }}
					</div>
					<div
						class="text-sm text-gray-600 mt-2 whitespace-nowrap overflow-hidden text-ellipsis"
					>
						{{ board.content }}
					</div>
					<div class="flex flex-row mt-1">
						<div
							class="w-14 h-12 rounded-full mr-2 overflow-hidden"
						>
							<img
								:src="
									board.profilePath
										? getImageSrc(board.profilePath)
										: getAvatar(board.nickName)
								"
								alt="Avatar"
								class="w-full h-full object-cover"
							/>
						</div>
						<div class="w-full flex justify-between">
							<div>
								<div>{{ board.nickName }}</div>
								<div class="text-sm text-gray-600">
									{{ getDiffDateFromToday(board.createdAt) }}
								</div>
							</div>
							<div
								v-if="
									auth.getLogin() &&
									board.userId !== auth.getUserId()
								"
								class="content-center"
							>
								<button
									v-if="board.follow"
									@click.stop="toggleFollow(board.userId)"
									class="btn p-1 text-sm rounded-3xl! bg-gray-200 w-[80px] hover:bg-gray-300"
								>
									팔로잉
								</button>
								<button
									v-else
									@click.stop="toggleFollow(board.userId)"
									class="btn p-1 text-sm rounded-3xl! bg-teal-500 w-[80px] hover:bg-teal-600 text-white"
								>
									팔로우
								</button>
							</div>
						</div>
					</div>
					<!-- 좋아요, 댓글수, 공유수 영역 -->
					<div
						class="flex flex-row border-t border-gray-200 mt-4 pt-2"
					>
						<div>
							<font-awesome-icon
								v-if="board.like"
								class="text-red-600 cursor-pointer text-lg"
								@click.stop="toggleLike(index, board.boardId)"
								:icon="['fas', 'heart']"
							/>
							<font-awesome-icon
								v-else
								class="cursor-pointer text-gray-600 text-lg"
								@click.stop="toggleLike(index, board.boardId)"
								:icon="['far', 'heart']"
							/>
							<span class="text-gray-600 text-lg ml-2">
								{{ board.likeCount.toLocaleString() }}
							</span>
						</div>
						<div class="ml-2">
							<font-awesome-icon
								class="text-gray-600 text-lg"
								:icon="['far', 'comment']"
							/>
							<span class="text-gray-600 text-lg ml-2">
								{{ board.commentCount.toLocaleString() }}
							</span>
						</div>
						<div class="ml-2">
							<font-awesome-icon
								class="text-gray-600 text-lg"
								:icon="['far', 'eye']"
							/>
							<span class="text-gray-600 text-lg ml-2">
								{{ board.viewCount.toLocaleString() }}
							</span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import type { SearchRequest } from "@/components/common/types/request/SearchRequest";
import type { BoardSearchFilter } from "./types/BoardSearchFilter";
import axios, { AxiosError, type AxiosResponse } from "axios";
import type { BoardSearchResponse } from "./types/response/BoardSearchResponse";
import type { BoardSearchInfo } from "./types/BoardSearchInfo";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import type { BoardLikeResponse } from "./types/response/BoardLikeResponse";
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";
import { useAuthStore } from "@/stores/Auth";
import { getDiffDateFromToday } from "@/utils/DateUtil";
import { useSpinnerStore } from "@/stores/Spinner";
import { useRouter } from "vue-router";
import { getAvatar, getImageSrc } from "@/utils/ImageUtil";

const props = defineProps<{
	filter: BoardSearchFilter;
	sortField: string;
}>();
const emit = defineEmits<{ (e: "totalCount", totalCount: number): void }>();
const auth = useAuthStore();
const router = useRouter();

const { startSpinner, endSpinner } = useSpinnerStore();

// 페이지 조회 공통 조건
const searchRequest = ref<SearchRequest<BoardSearchFilter>>({
	filter: props.filter,
	sortField: "created_at",
	sortDirection: "ASC",
	page: 0,
	size: 9,
});

const boardList = ref<BoardSearchInfo[]>();
const likeLoading = ref<boolean>(false); // 좋아요 로딩
const followLoading = ref<boolean>(false); // 팔로우 로딩

// 팔로우 토글
const toggleFollow = (userId: string) => {
	if (!auth.getLogin()) {
		alert("로그인이 필요한 서비스입니다.");
		return;
	}
	if (!followLoading.value) {
		followLoading.value = true;
		axios
			.patch(`/web/api/board/follow/${userId}`)
			.then(() => {
				// 팔로우 토글 성공 후 화면에서 해당 유저의 게시글 전부 토글
				if (boardList.value) {
					boardList.value.forEach((board) => {
						if (board.userId === userId) {
							board.follow = !board.follow;
						}
					});
				}
			})
			.catch((res: AxiosError<ErrorResponse>) => {
				alert(res.response?.data.errorMessage);
			})
			.finally(() => {
				followLoading.value = false;
			});
	}
};
// 게시글 좋아요
const toggleLike = (index: number, boardId: number) => {
	if (!auth.getLogin()) {
		alert("로그인이 필요한 서비스입니다.");
		return;
	}
	if (!likeLoading.value) {
		likeLoading.value = true;
		axios
			.patch(`/web/api/board/like/${boardId}`)
			.then((res: AxiosResponse<BoardLikeResponse>) => {
				if (boardList.value && boardList.value[index]) {
					boardList.value[index].like = res.data.liked; // 현재 좋아요 상태 갱신
					boardList.value[index].likeCount = res.data.likeCount; // 해당 게시글 좋아요 개수
				}
			})
			.catch((res: AxiosError<ErrorResponse>) => {
				alert(res.response?.data.errorMessage);
			})
			.finally(() => {
				likeLoading.value = false;
			});
	}
};
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
			if (!res.data.login) {
				auth.setLogout();
			}
			boardList.value = res.data.boardSearchInfo;
			emit("totalCount", res.data.totalCount);
		})
		.catch()
		.finally(() => {
			endSpinner();
		});
};

// 게시글 정보 조회 함수를 부모에서 접근 가능하게 허용
defineExpose({ getNextBoardList });
</script>
<style scoped></style>

<template>
	<div class="mb-10">
		<div class="w-full justify-items-center relative">
			<img
				v-if="boardDetailInfo"
				class="w-[full] aspect-[2/1] object-cover"
				:src="'/web/api/board/feature/' + boardDetailInfo?.boardId"
			/>
			<!-- 대표 이미지 영역 안 -->
			<div class="absolute w-full top-0">
				<div class="flex justify-between">
					<div
						@click="router.back()"
						class="bg-white/50 rounded-3xl w-[40px] h-[40px] text-center content-center text-white ml-5 mt-5 cursor-pointer"
					>
						<font-awesome-icon :icon="['fa', 'arrow-left']" />
					</div>
					<div
						class="bg-white/50 rounded-3xl w-[70px] h-[40px] text-center content-center mt-5 mr-5"
					>
						<font-awesome-icon
							v-if="boardDetailInfo?.like"
							class="text-red-600 cursor-pointer text-lg"
							@click="toggleLike(boardDetailInfo.boardId)"
							:icon="['fas', 'heart']"
						/>
						<font-awesome-icon
							v-else
							class="cursor-pointer text-gray-600 text-lg"
							@click="toggleLike(boardDetailInfo?.boardId)"
							:icon="['far', 'heart']"
						/>
						<span class="text-gray-600 text-lg ml-2">
							{{ boardDetailInfo?.likeCount ?? 0 }}
						</span>
					</div>
				</div>
			</div>
			<!-- 대표 이미지 안 제목 -->
			<div
				class="absolute bottom-0 text-white text-3xl font-bold p-5 w-full"
			>
				{{ boardDetailInfo?.title }}
			</div>
		</div>
		<!-- 게시글 내용 프레임 -->
		<div class="pr-30 pl-30">
			<div class="flex items-center justify-between pt-10 pb-5 text-sm">
				<!-- 왼쪽: 시간, 조회수 -->
				<div class="flex items-center gap-3 text-gray-600">
					<span>
						{{ getDiffDateFromToday(boardDetailInfo?.createdAt) }}
					</span>

					<span class="text-gray-300">·</span>

					<div class="flex items-center gap-1">
						<font-awesome-icon :icon="['far', 'eye']" />
						<span>{{ boardDetailInfo?.viewCount }}</span>
					</div>
				</div>

				<!-- 오른쪽: 수정, 삭제 -->
				<div class="flex items-center gap-2">
					<button
						class="text-gray-400 hover:text-gray-700 cursor-pointer"
					>
						수정
					</button>

					<span class="text-gray-300">·</span>

					<button
						@click="deleteBoard"
						class="text-gray-400 hover:text-red-500 cursor-pointer"
					>
						삭제
					</button>
				</div>
			</div>
			<!-- 작성자 프로필 -->
			<div
				class="bg-white rounded-2xl w-full h-[100px] items-center pr-5 pl-5 flex justify-between"
			>
				<div class="flex flex-row mt-1">
					<img
						:src="getAvatar('nick')"
						alt="Avatar"
						class="w-12 h-12 object-cover"
					/>
					<div>
						<div class="font-bold text-lg">
							{{ boardDetailInfo?.nickName }}
						</div>
						<!-- TODO: 닉네임 밑에 자기소개? -->
						<!-- <div class="text-gray-600 text-sm">
							{{ boardDetailInfo?.nickName }}
						</div> -->
					</div>
				</div>
				<div class="flex flex-row mt-1">
					<div class="text-gray-600 text-md">
						게시글 {{ boardDetailInfo?.commentCount }}개
					</div>
					<div class="ml-2">
						<button
							class="btn p-1 text-sm rounded-3xl! bg-teal-500 w-[80px] hover:bg-teal-600 text-white"
						>
							팔로우
						</button>
					</div>
				</div>
			</div>
			<!-- 본문 내용 -->
			<div>
				<ContentEditor
					v-if="boardDetailInfo.contentHtml !== ''"
					v-model="boardDetailInfo.contentHtml"
					:editable="false"
				/>
			</div>
			<!-- 태그 -->
			<div class="mt-5 mb-10">
				<div class="text-gray-600 text-md mb-2">관련 태그</div>
				<div>
					<span
						v-for="tag in boardDetailInfo.tags"
						class="bg-gray-100 rounded-xl py-1 px-3 mr-2"
						>#{{ tag }}</span
					>
				</div>
			</div>
			<!-- 좋아요, 공유 버튼? -->
			<div
				class="bg-white rounded-2xl w-full h-[100px] items-center pr-5 pl-5 flex justify-between"
			>
				<div class="flex flex-row mt-1">
					<div
						v-if="boardDetailInfo?.like"
						class="rounded-3xl bg-red-100 w-[50px] h-[50px] text-center content-center mr-2"
					>
						<font-awesome-icon
							class="text-red-600 cursor-pointer text-lg"
							@click="toggleLike(boardDetailInfo.boardId)"
							:icon="['fas', 'heart']"
						/>
					</div>
					<div
						v-else
						class="rounded-3xl bg-gray-100 w-[50px] h-[50px] text-center content-center mr-2"
					>
						<font-awesome-icon
							class="cursor-pointer text-gray-600 text-lg"
							@click="toggleLike(boardDetailInfo?.boardId)"
							:icon="['far', 'heart']"
						/>
					</div>
					<div>
						<div class="font-bold text-lg">
							{{ boardDetailInfo?.likeCount }}
						</div>
						<div class="text-gray-600 text-sm">
							이 글이 도움이 되었나요?
						</div>
					</div>
				</div>
				<div class="flex flex-row mt-1">
					<div class="ml-2">
						<!-- 플랫폼 별 공유 버튼? 카카오 등? -->
						<!-- <button
							class="btn p-1 text-sm rounded-3xl! bg-teal-500 w-[80px] hover:bg-teal-600 text-white"
						>
							팔로우
						</button> -->
					</div>
				</div>
			</div>
			<!-- 댓글 영역 -->
			<div class="mt-10">
				<span class="text-xl font-bold"
					>댓글 {{ boardDetailInfo.commentCount }}개</span
				>
				<div
					v-if="auth.isLogin"
					class="mt-5 bg-white rounded-2xl w-full px-5 py-5"
				>
					<div class="flex flex-row w-full">
						<div class="w-[50px] h-[50px] rounded-lg mr-2">
							<img
								v-if="auth.getProfileImagePath()"
								class="rounded-full w-[40px] h-[40px]"
								:src="getImageSrc(auth.getProfileImagePath())"
							/>
							<img
								v-else
								:src="getAvatar(auth.getNickName())"
								alt="Avatar"
								class="rounded-full w-[40px] h-[40px] bg-gray-300"
							/>
						</div>
						<div class="w-full">
							<div>
								<textarea
									v-model="commentContent"
									@input="limitCommentLength(0)"
									class="w-full h-[100px] resize-none hover-green border-gray rounded-md custom-input p-2"
									placeholder="댓글을 남겨보세요."
								></textarea>
							</div>
							<div class="mt-5 w-full flex justify-between">
								<div>{{ commentContent.length }}/500</div>
								<div>
									<button
										@click="addComment(0, 0)"
										class="btn bg-gray-800 font-bold text-white py-2 px-4 !rounded-3xl"
									>
										댓글 작성
									</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- 댓글 -->
				<div class="mt-8">
					<div v-for="comment in commentList" class="mt-4">
						<!-- 원댓글 -->
						<div
							v-if="comment.parentId === 0"
							class="bg-white rounded-2xl px-5 py-5"
						>
							<div
								v-if="comment.isDel"
								class="py-4 pl-5 text-sm text-gray-400"
							>
								삭제된 댓글입니다.
							</div>
							<div v-if="!comment.isDel" class="flex">
								<div class="mr-3">
									<img
										v-if="comment.profileImagePath"
										class="rounded-full w-[40px] h-[40px]"
										:src="
											getImageSrc(
												comment.profileImagePath,
											)
										"
									/>
									<img
										v-else
										:src="getAvatar(comment.nickName)"
										alt="Avatar"
										class="rounded-full w-[40px] h-[40px] bg-gray-300"
									/>
								</div>

								<div class="flex-1">
									<div>
										<span class="font-bold text-sm">{{
											comment.nickName
										}}</span>
										<span
											class="text-xs text-gray-400 ml-2"
										>
											{{
												getDiffDateFromToday(
													comment.createdAt,
												)
											}}
										</span>
									</div>

									<div
										class="mt-2 text-sm leading-6 break-words"
									>
										{{ comment.content }}
									</div>

									<div
										v-if="auth.isLogin"
										class="mt-4 flex gap-4 text-xs text-gray-400"
									>
										<!-- <button>♡ 0</button> -->
										<button
											@click="
												openReCommentInput(comment.id)
											"
											class="cursor-pointer"
										>
											답글
										</button>
										<span
											v-if="
												comment.userId ===
												auth.getUserId()
											"
											class="text-xs text-gray-400"
											>·</span
										>
										<button
											v-if="
												comment.userId ===
												auth.getUserId()
											"
											@click="deleteComment(comment.id)"
											class="cursor-pointer text-red-500"
										>
											삭제
										</button>
									</div>
								</div>
							</div>
						</div>

						<!-- 대댓글 -->
						<div v-else class="ml-[60px] mt-2">
							<div
								v-if="comment.isDel"
								class="border-l-2 border-teal-300 pl-4 py-4 text-sm text-gray-400"
							>
								삭제된 댓글입니다.
							</div>
							<div
								v-if="!comment.isDel"
								class="border-l-2 border-teal-300 pl-4 py-4 flex"
							>
								<div class="mr-3">
									<img
										v-if="comment.profileImagePath"
										class="rounded-full w-[40px] h-[40px]"
										:src="
											getImageSrc(
												comment.profileImagePath,
											)
										"
									/>
									<img
										v-else
										:src="getAvatar(comment.nickName)"
										alt="Avatar"
										class="rounded-full w-[40px] h-[40px]"
									/>
								</div>

								<div>
									<div>
										<span class="font-bold text-sm">{{
											comment.nickName
										}}</span>
										<span
											class="text-xs text-gray-400 ml-2"
										>
											{{
												getDiffDateFromToday(
													comment.createdAt,
												)
											}}
										</span>
									</div>

									<div class="mt-1 text-sm">
										{{ comment.content }}
									</div>
									<div
										class="mt-4 flex gap-4 text-xs text-gray-400"
									>
										<button
											v-if="
												comment.userId ===
												auth.getUserId()
											"
											@click="deleteComment(comment.id)"
											class="cursor-pointer text-red-500"
										>
											삭제
										</button>
									</div>
								</div>
							</div>
						</div>
						<!-- 답글 입력 -->
						<div
							v-if="reCommentInputOpen === comment.id"
							class="mt-2 flex items-center gap-2"
						>
							<textarea
								v-model="reCommentContent"
								@input="limitCommentLength(1)"
								class="w-full h-[60px] resize-none border border-gray-200 rounded-xl px-3 py-2 outline-none focus:border-teal-400"
								placeholder="답글을 입력하세요."
							></textarea>

							<button
								@click="addComment(comment.id, 1)"
								class="btn bg-gray-800 font-bold text-white rounded-2xl px-4 py-2 shrink-0 cursor-pointer"
							>
								등록
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>
<script setup lang="ts">
import axios, { AxiosError, type AxiosResponse } from "axios";
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import type { BoardDetailResponse } from "./types/response/BoardDetailResponse";
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";
import { useSpinnerStore } from "@/stores/Spinner";
import { useAuthStore } from "@/stores/Auth";
import type { BoardLikeResponse } from "../search/types/response/BoardLikeResponse";
import { getDiffDateFromToday } from "@/utils/DateUtil";
import ContentEditor from "@/components/tiptab/ContentEditor.vue";
import type { BoardCommentInfo } from "./types/BoardCommentInfo";
import type { BoardCommentRequest } from "./types/request/BoardCommentRequest";
import type { BoardCommentResponse } from "./types/response/BoardCommentResponse";

const route = useRoute();
const router = useRouter();
const auth = useAuthStore();
const { startSpinner, endSpinner } = useSpinnerStore();

const MAX_COMMENT_LENGTH = 500; // 댓글 글자 수 제한
const boardDetailInfo = ref<BoardDetailResponse>({
	// 게시글 정보
	boardId: 0,
	follow: false,
	title: "",
	contentHtml: "",
	likeCount: 0,
	sharedCount: 0,
	commentCount: 0,
	viewCount: 0,
	createdAt: new Date(),
	like: false,
	travelStartAt: new Date(),
	travelEndAt: new Date(),
	tags: [],
	boardFeatureId: "",
	profileImagePath: "",
	userId: "",
	nickName: "",
	bio: "",
	boardCount: 0,
});
const commentAddInfo = ref<BoardCommentRequest>({
	// 댓글 등록 요청 정보
	boardId: 0,
	parentId: 0,
	content: "",
	depth: 0,
});
const commentContent = ref<string>(""); // 댓글
const reCommentContent = ref<string>(""); // 대댓글
const commentList = ref<BoardCommentInfo[]>([]); // 댓글 정보
const likeLoading = ref<boolean>(false);
const reCommentInputOpen = ref<number | null>(null); // 대댓글 입력 창 오픈 - 댓글의 id로 구분

// 프로필 이미지 경로 반환
const getImageSrc = (path: string) => {
	return path.startsWith("http")
		? path
		: `/web/api/file?path=${encodeURIComponent(path)}`;
};
// 댓글 500자 제한
const limitCommentLength = (depth: number) => {
	if (depth === 0) {
		commentContent.value = commentContent.value.substring(
			0,
			MAX_COMMENT_LENGTH,
		);
	} else if (depth === 1) {
		reCommentContent.value = reCommentContent.value.substring(
			0,
			MAX_COMMENT_LENGTH,
		);
	}
};
// 댓글 삭제
const deleteComment = (commentId: number) => {
	startSpinner();
	axios
		.delete(`/web/api/board/comment/${commentId}`)
		.then((res: AxiosResponse<BoardCommentResponse>) => {
			commentList.value = res.data.boardCommentList;
			boardDetailInfo.value.commentCount = res.data.totalCount;
		})
		.catch(() => {
			alert("댓글 삭제를 실패하였습니다.");
		})
		.finally(() => {
			endSpinner();
		});
};
// 대댓글 입력창 오픈
const openReCommentInput = (commentId: number) => {
	if (reCommentInputOpen.value === commentId) {
		reCommentInputOpen.value = null;
		return;
	}
	reCommentInputOpen.value = commentId;
};
// 댓글 작성
const addComment = (parentId: number, depth: number) => {
	if (depth === 0) {
		commentAddInfo.value.content = commentContent.value; // 댓글
	} else if (depth === 1) {
		commentAddInfo.value.content = reCommentContent.value; // 대댓글
	}

	if (!commentAddInfo.value.content) {
		alert("댓글을 입력해주세요.");
		return;
	}
	startSpinner();
	commentAddInfo.value.boardId = boardDetailInfo.value.boardId;
	commentAddInfo.value.parentId = parentId;
	axios
		.post("/web/api/board/comment", commentAddInfo.value)
		.then((res: AxiosResponse<BoardCommentResponse>) => {
			commentList.value = res.data.boardCommentList;
			boardDetailInfo.value.commentCount = res.data.totalCount;
		})
		.catch((res: AxiosError<ErrorResponse>) => {
			alert(res.response?.data.errorMessage);
		})
		.finally(() => {
			endSpinner();
			commentContent.value = "";
			reCommentContent.value = "";
			commentAddInfo.value.content = "";
			reCommentInputOpen.value = null; // 대댓글 입력창 닫기
		});
};
// 게시글 댓글 조회
const getCommentList = (boardId: string | string[] | undefined) => {
	startSpinner();

	axios
		.get(`/web/api/board/comment/${boardId}`)
		.then((res: AxiosResponse<BoardCommentResponse>) => {
			commentList.value = res.data.boardCommentList;
		})
		.catch((res: AxiosError<ErrorResponse>) => {
			alert(res.response?.data.errorMessage);
		})
		.finally(() => {
			endSpinner();
		});
};
// 게시글 좋아요
const toggleLike = (boardId: number | undefined) => {
	if (!boardId) {
		alert("유효하지 않은 요청입니다.");
		return;
	}
	if (!auth.getLogin()) {
		alert("로그인이 필요한 서비스입니다.");
		return;
	}
	if (!likeLoading.value) {
		likeLoading.value = true;
		axios
			.patch(`/web/api/board/like/${boardId}`)
			.then((res: AxiosResponse<BoardLikeResponse>) => {
				if (boardDetailInfo.value) {
					boardDetailInfo.value.like = res.data.liked; // 현재 좋아요 상태 갱신
					boardDetailInfo.value.likeCount = res.data.likeCount; // 해당 게시글 좋아요 개수
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
const getBoardDetail = async (boardId: string | string[] | undefined) => {
	startSpinner();
	if (typeof boardId !== "string" && typeof boardId !== "number") {
		alert("유효하지 않은 접근입니다.");
		return;
	}

	await axios
		.get(`/web/api/board/${boardId}`)
		.then((res: AxiosResponse<BoardDetailResponse>) => {
			boardDetailInfo.value = res.data;
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			alert(error.response?.data.errorMessage);
			router.back();
		})
		.finally(() => {
			endSpinner();
		});
};
// nickName으로 아바타 생성
const getAvatar = (nickName: string) => {
	const url = new URL("https://api.dicebear.com/10.x/lorelei/svg");
	url.searchParams.set("seed", nickName);
	url.searchParams.set("size", "50");

	return url.href;
};
onMounted(() => {
	getBoardDetail(route.params.id);
	getCommentList(route.params.id);
});
</script>

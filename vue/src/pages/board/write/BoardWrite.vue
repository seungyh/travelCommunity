<template>
	<div class="board-frame">
		<div class="flex justify-between pl-5 pr-5 mt-10 mb-2">
			<div>
				<h2 class="text-2xl font-[700]">새 여행 이야기</h2>
				<span>여행의 소중한 순간을 기록하세요</span>
			</div>
			<div class="flex flex-row">
				<button
					@click="draft"
					class="white-btn h-[40px] w-[100px] mr-2"
				>
					<font-awesome-icon :icon="['far', 'floppy-disk']" />
					임시저장
				</button>
				<button
					@click="publish"
					class="btn bg-teal-500 h-[40px] w-[100px] text-white"
				>
					<font-awesome-icon :icon="['far', 'paper-plane']" />
					등록하기
				</button>
			</div>
		</div>
		<div class="write-area">
			<!-- 제목 -->
			<div>
				<input
					v-model="boardData.title"
					@keydown="limitTitleLength()"
					type="text"
					placeholder="여행의 제목을 입력하세요..."
					class="h-[40px] text-2xl w-full font-[700] board-title custom-input"
				/>
				<div class="text-right">
					<span class="text-gray-400"
						>{{ boardData.title.length }}/100</span
					>
				</div>
			</div>
			<!-- 카테고리, 여행 기간 선택 -->
			<div class="flex flex-row mt-10">
				<div class="w-[50%]">
					<span class="text-gray-400">카테고리</span>
					<CustomSelect
						v-model="boardData.category"
						:placeholder="'카테고리를 선택해주세요.'"
						:selectedItem="selectedItem"
						:items="categoryMenus"
						class="h-[40px] border-gray rounded-md p-2 w-[96%] hover-green"
					></CustomSelect>
				</div>
				<div class="w-[50%]">
					<span class="text-gray-400">여행 기간</span>
					<div class="flex flex-row justify-between">
						<div
							class="hover-green relative border-gray rounded-md p-1 pt-1.5 pl-6 w-[48%]"
						>
							<flat-pickr
								v-model="boardData.travelStartAt"
								:config="config"
								class="form-control w-[96%] cursor-pointer"
								placeholder="시작한 여행 날짜를 선택하세요"
								name="date"
							/>
							<span
								class="absolute text-gray-500 -translate-y-1/2 pointer-events-none left-1 top-1/2 dark:text-gray-400"
							>
								<i class="bi bi-calendar4"></i>
							</span>
						</div>
						<span class="content-center">~</span>
						<div
							class="hover-green relative border-gray rounded-md p-1 pt-1.5 pl-6 w-[48%]"
						>
							<flat-pickr
								v-model="boardData.travelEndAt"
								:config="config"
								class="form-control w-[96%] cursor-pointer"
								placeholder="마지막 여행 날짜를 선택하세요"
								name="date"
							/>
							<span
								class="absolute text-gray-500 -translate-y-1/2 pointer-events-none left-1 top-1/2 dark:text-gray-400"
							>
								<i class="bi bi-calendar4"></i>
							</span>
						</div>
					</div>
				</div>
			</div>
			<!-- 태그, 여행 장소 입력 -->
			<div class="flex flex-row mt-5">
				<div class="w-[50%]">
					<span class="text-gray-400">태그</span>
					<TagEditor
						v-model="boardData.tags"
						class="hover-green min-h-[40px] rounded-md w-[95%]"
					></TagEditor>
				</div>
				<div class="w-[50%]">
					<span class="text-gray-400">여행 장소</span>
					<div class="w-full relative">
						<input
							type="text"
							v-model="boardData.place"
							placeholder="여행한 장소를 입력하세요. ex) 일본"
							class="hover-green h-[40px] relative border-gray rounded-md p-1 pt-1 pb-2 custom-input pl-6 w-full"
						/>
						<span
							class="absolute text-gray-500 -translate-y-1/2 pointer-events-none left-1 bottom-0 dark:text-gray-400"
						>
							<i class="bi bi-geo-alt-fill"></i>
						</span>
					</div>
				</div>
			</div>
			<!-- 대표 이미지 선택 -->
			<div class="mt-5">
				<div>
					<span class="text-gray-400">대표 이미지</span>
					<!-- 대표 이미지 미리보기 -->
					<div
						v-if="previewSrc"
						class="w-full h-full text-center content-center relative group"
					>
						<img
							:src="previewSrc"
							class="w-full h-[300px] overflow-hidden object-cover"
							alt="미리보기"
						/>
						<!-- 이미지 hover시 반투명 및 버튼 -->
						<div
							class="hidden group-hover:block bg-black/50 absolute w-full h-[300px] z-100 top-0 rounded-md content-center"
						>
							<button
								@click="openFeaturedImageFileExplorer"
								class="white-btn w-[80px] h-[40px] mr-2"
							>
								<i class="bi bi-image"></i>
								변경
							</button>
							<button
								@click="deleteFeatureImage"
								class="white-btn w-[80px] h-[40px] text-red-500"
							>
								<font-awesome-icon
									:icon="['far', 'trash-can']"
								/>
								삭제
							</button>
						</div>
					</div>
					<div
						v-else
						@click="openFeaturedImageFileExplorer"
						class="w-full border-dashed border-gray border-2! hover-green hover:bg-teal-50 cursor-pointer rounded-md h-[100px] bg-gray-100"
					>
						<div class="w-full h-full text-center content-center">
							<i class="bi bi-image text-3xl text-teal-400"></i>
							<div>
								<span class="text-gray-500"
									>클릭하여 이미지 업로드</span
								>
							</div>
							<div>
								<span class="text-sm text-gray-400">
									권장 크기: 1200x800px, 최대 5MB
								</span>
							</div>
						</div>
					</div>
					<input
						@change="uploadImg($event, 'FEATURE')"
						ref="fileInput"
						type="file"
						:multiple="false"
						hidden
						accept=".jpg, .jpeg, .png"
					/>
				</div>
			</div>
			<!-- 내용 editor -->
			<div class="mt-5">
				<span class="text-gray-500">본문 내용</span>
				<div
					class="w-full min-h-[500px] rounded-md border-gray mt-2 hover-green"
				>
					<ContentEditor
						v-model="boardData.contentValue"
						@draft="draft"
					/>
				</div>
			</div>
			<!-- 추가 이미지 -->
			<div class="mt-5">
				<div class="flex justify-between">
					<span class="text-gray-500">추가 이미지</span>
					<span class="text-gray-500 mr-2"
						>{{ extraSrcs?.length ?? 0 }}/8</span
					>
				</div>
				<div>
					<span class="text-xs text-gray-400"
						>여행 중 찍은 사진들을 올려보세요.</span
					>
				</div>
				<div class="flex flex-wrap mt-2">
					<div
						v-for="(id, index) in extraImgIds"
						class="mr-2 mb-2 border-gray"
					>
						<div
							class="w-[250px] h-[200px] text-center content-center relative group"
						>
							<img
								:src="`/web/api/board/file/${id}`"
								class="w-[250px] h-[200px] overflow-hidden object-cover"
								alt="미리보기"
							/>
							<!-- 이미지 hover시 반투명 및 버튼 -->
							<div
								class="hidden group-hover:block bg-black/50 absolute w-[250px] h-[200px] z-100 top-0 rounded-md content-center"
							>
								<button
									@click="deleteExtraImage(index)"
									class="white-btn w-[80px] h-[40px] text-red-500"
								>
									<font-awesome-icon
										:icon="['far', 'trash-can']"
									/>
									삭제
								</button>
							</div>
						</div>
					</div>
					<div
						v-if="extraImgIds.length < 8"
						@click="openExtraImageFileExplorer"
						class="border-dashed border-gray border-2! hover-green hover:bg-teal-50 cursor-pointer rounded-md h-[200px] w-[250px]"
					>
						<div class="w-full h-full text-center content-center">
							<div>
								<span class="text-gray-400 text-sm"
									><font-awesome-icon :icon="['fas', 'plus']"
								/></span>
							</div>
							<div>
								<span class="text-sm text-gray-400">
									추가
								</span>
							</div>
						</div>
					</div>
					<input
						@change="uploadImg($event, 'EXTRA')"
						ref="extraFileInput"
						type="file"
						:multiple="false"
						hidden
						accept=".jpg, .jpeg, .png"
					/>
				</div>
			</div>
			<!-- 공개 설정 -->
			<div class="mt-5 mb-5">
				<span class="text-gray-500">공개 설정</span>
				<div class="flex justify-between mt-2">
					<div
						@click="boardData.visibility = 'PUBLIC'"
						class="border-gray border-2! hover-green hover:bg-teal-50 cursor-pointer rounded-4xl h-[200px] w-[350px]"
						:class="
							boardData.visibility === 'PUBLIC'
								? 'bg-teal-50 border-green'
								: ''
						"
					>
						<div class="w-full h-full text-center content-center">
							<div>
								<span class="text-gray-400 text-sm"
									><font-awesome-icon
										:icon="['fas', 'earth']"
								/></span>
							</div>
							<div>
								<span class="text-md text-gray-500">
									전체 공개
								</span>
							</div>
							<div>
								<span class="text-sm text-gray-400">
									모든 사용자가 볼 수 있습니다
								</span>
							</div>
						</div>
					</div>
					<div
						@click="boardData.visibility = 'FOLLOW'"
						class="border-gray border-2! hover:text-teal-50! hover-green hover:bg-teal-50 cursor-pointer rounded-4xl h-[200px] w-[350px]"
						:class="
							boardData.visibility === 'FOLLOW'
								? 'bg-teal-50 border-green'
								: ''
						"
					>
						<div class="w-full h-full text-center content-center">
							<div>
								<span class="text-gray-400 text-sm"
									><font-awesome-icon
										:icon="['fas', 'people-group']"
								/></span>
							</div>
							<div>
								<span class="text-md text-gray-500">
									팔로워 공개</span
								>
							</div>
							<div>
								<span class="text-sm text-gray-400">
									팔로워만 볼 수 있습니다
								</span>
							</div>
						</div>
					</div>
					<div
						@click="boardData.visibility = 'PRIVATE'"
						class="border-gray border-2! hover-green hover:bg-teal-50 cursor-pointer rounded-4xl h-[200px] w-[350px]"
						:class="
							boardData.visibility === 'PRIVATE'
								? 'bg-teal-50 border-green'
								: ''
						"
					>
						<div class="w-full h-full text-center content-center">
							<div>
								<span class="text-gray-400 text-sm"
									><font-awesome-icon :icon="['fas', 'lock']"
								/></span>
							</div>
							<div>
								<span class="text-md text-gray-500">
									비공개</span
								>
							</div>
							<div>
								<span class="text-sm text-gray-400">
									나만 볼 수 있습니다
								</span>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- 임시 저장, 등록 버튼 -->
		<div class="flex justify-between pl-5 pr-5 mt-10 mb-30">
			<div @click="router.back()">
				<span class="cursor-pointer"
					><font-awesome-icon :icon="['fa', 'arrow-left']" />
					돌아가기</span
				>
			</div>
			<div class="flex flex-row">
				<button
					@click="draft"
					class="white-btn h-[40px] w-[100px] mr-2"
				>
					<font-awesome-icon :icon="['far', 'floppy-disk']" />
					임시저장
				</button>
				<button
					@click="publish"
					class="btn bg-teal-500 h-[40px] w-[100px] text-white"
				>
					<font-awesome-icon :icon="['far', 'paper-plane']" />
					등록하기
				</button>
			</div>
		</div>
	</div>
</template>
<script setup lang="ts">
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { onMounted, ref } from "vue";
import type { BoardRequest } from "./types/request/BoardRequest";
import CustomSelect from "@/components/CustomSelect.vue";
import type { SelectItem } from "@/components/common/types/SelectItem";
import { Korean } from "flatpickr/dist/l10n/ko.js";
import flatPickr from "vue-flatpickr-component";
import TagEditor from "@/components/tiptab/TagEditor.vue";
import ContentEditor from "@/components/tiptab/ContentEditor.vue";
import axios, { AxiosError, type AxiosResponse } from "axios";
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";
import router from "@/router";
import type { DraftResponse } from "./types/response/DraftResponse";
import { useSpinnerStore } from "@/stores/Spinner";

const { startSpinner, endSpinner } = useSpinnerStore();

const previewSrc = ref<string>(""); // 대표 이미지 미리보기
const extraSrcs = ref<string[]>([]); // 추가 이미지
const fileInput = ref<HTMLInputElement | null>(null); // 대표 이미지 input 주소값
const extraFileInput = ref<HTMLInputElement | null>(null); // 추가 이미지 input 주소값
const extraImgIds = ref<number[]>([]); // 추가 이미지 file id
const selectedItem = ref<SelectItem>();
const featureImgId = ref<number | null>(null); // 대표 이미지 파일 id
// 작성한 게시글 내용
const boardData = ref<BoardRequest>({
	boardId: 0,
	title: "",
	contentValue: {
		content: "",
		contentHtml: "",
	},
	travelStartAt: null,
	travelEndAt: null,
	category: "",
	tags: [],
	place: "",
	visibility: "PUBLIC", // 전체 공개 PUBLIC, 팔로워 공개 FOLLOW, 비공개 PRIVATE
	status: "DRAFT",
});
// 여행 카테고리 선택 Item
const categoryMenus = ref<SelectItem[]>([
	{ label: "국내 여행", value: "DOMESTIC" },
	{ label: "해외 여행", value: "OVERSEAS" },
	{ label: "맛집탐방", value: "FOOD" },
	{ label: "숙소리뷰", value: "ACCOMMODATION" },
	{ label: "여행 팁", value: "TIP" },
]);

const config = ref({
	dateFormat: "Y-m-d",
	wrap: true,
	clickOpens: true,
	allowInput: true,
	disableMobile: true,
	static: false,
	appendTo: document.body,
	positionElement: undefined,
	locale: Korean,
});

// 제목 길이 100자 제한
const limitTitleLength = () => {
	boardData.value.title = boardData.value.title.substring(0, 99);
};

// 임시 저장
const draft = () => {
	startSpinner();
	boardData.value.status = "DRAFT";
	axios
		.post("/web/api/board/draft", boardData.value)
		.then((res: AxiosResponse<number>) => {
			boardData.value.boardId = res.data;
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			console.log(error);
			alert(error.response?.data.errorMessage);
		})
		.finally(() => {
			endSpinner();
		});
};

// 게시글 등록
const publish = () => {
	boardData.value.title = boardData.value.title
		? boardData.value.title.trim()
		: "";
	boardData.value.place = boardData.value.place
		? boardData.value.place.trim()
		: "";
	if (!validCheck()) {
		return;
	}
	boardData.value.status = "PUBLISHED";
	startSpinner();
	axios
		.post("/web/api/board/write", boardData.value)
		.then((res: AxiosResponse<number>) => {
			// 게시글 등록일때
			alert("게시글 등록을 성공하였습니다.");
			router.push("/");
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			console.log(error);
			alert(error.response?.data.errorMessage);
		})
		.finally(() => {
			endSpinner();
		});
};
// 게시글 등록 유효성 체크
const validCheck = () => {
	if (!boardData.value.title) {
		alert("제목을 입력해주세요.");
		return false;
	}
	if (!boardData.value.place) {
		alert("여행 장소를 입력해주세요.");
		return false;
	}
	if (!boardData.value.travelStartAt || !boardData.value.travelEndAt) {
		alert("여행 날짜를 입력해주세요.");
		return false;
	}
	if (boardData.value.travelStartAt > boardData.value.travelEndAt) {
		alert("올바른 여행 날짜를 입력해주세요.");
		return false;
	}
	if (!boardData.value.category) {
		alert("카테고리를 입력해주세요.");
		return false;
	}
	const tmpContent = boardData.value.contentValue.contentHtml;

	if (tmpContent === "<p></p>" || tmpContent === "") {
		alert("내용을 입력해주세요.");
		return false;
	}
	return true;
};

// 대표 이미지 삭제
const deleteFeatureImage = async () => {
	if (featureImgId.value) {
		await deleteImage(featureImgId.value);
		featureImgId.value = null;
		releaseFeatureImgMemory();
	}
};
// 추가 이미지 삭제
const deleteExtraImage = async (index: number) => {
	const fileId = extraImgIds.value[index];
	if (fileId) {
		await deleteImage(fileId);
		if (extraImgIds.value && extraImgIds.value[index]) {
			extraImgIds.value.splice(index, 1); // 저장된 파일 정보 삭제
		}
	}
};

const deleteImage = async (fileId: number) => {
	startSpinner();
	await axios
		.delete(`/web/api/board/file/${fileId}`)
		.catch(() => {
			alert("파일 삭제를 실패하였습니다.");
		})
		.finally(() => {
			endSpinner();
		});
};
// 대표 이미지 등록
const uploadImg = (event: Event, type: string) => {
	const target = event.target as HTMLInputElement; // 선택한 파일 가져오기
	const file = target.files?.[0];
	const MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

	if (file && file.size > MAX_FILE_SIZE) {
		alert("파일 크기는 5MB 이하만 업로드할 수 있습니다.");
		target.value = "";
		return;
	}

	const formData = new FormData();
	if (file) {
		formData.append("file", file);
	}
	const jsonBlob = new Blob([JSON.stringify(type)], {
		type: "application/json",
	});
	formData.append("type", jsonBlob);
	// 파일 업로드
	startSpinner();
	axios
		.post("/web/api/board/file/upload", formData, {
			headers: { "Content-Type": "multipart/form-data" },
		})
		.then((res: AxiosResponse<number>) => {
			if (type === "FEATURE") {
				// 대표 이미지 미리보기 메모리 해제
				releaseFeatureImgMemory();

				if (file) {
					// 미리보기 이미지 생성
					previewSrc.value = URL.createObjectURL(file);
					featureImgId.value = res.data;
				}
			} else {
				// 추가 이미지
				extraImgIds.value.push(res.data);
			}
		})
		.catch((res: AxiosError<ErrorResponse>) => {
			alert("파일 업로드 실패하였습니다.");
			target.value = "";
		})
		.finally(() => {
			endSpinner();
		});
};
const openExtraImageFileExplorer = () => {
	extraFileInput.value?.click();
};
// 대표 이미지 파일 탐색기 열기
const openFeaturedImageFileExplorer = () => {
	fileInput.value?.click();
};

// 대표 이미지 미리보기 메모리 해제
const releaseFeatureImgMemory = () => {
	if (previewSrc.value) {
		URL.revokeObjectURL(previewSrc.value); // url 미리보기 메모리 해제
		previewSrc.value = ""; // 미리보기 url 초기화
	}
};
// 임시 저장 데이터 삭제
const deleteDraft = (boardId: number) => {};
// 임시저장된 게시글 조회
const getDraft = () => {
	startSpinner();
	axios
		.get("/web/api/board/draft")
		.then((res: AxiosResponse<DraftResponse>) => {
			const data = res.data;
			if (!data) {
				// 임시저장된 데이터 없음
				return;
			}
			if (
				confirm(
					"이전에 임시저장한 게시글이 있습니다. 불러오시겠습니까?",
				)
			) {
				boardData.value.boardId = data.boardId; // 게시글 id
				boardData.value.category = data.category; // 카테고리
				boardData.value.contentValue = {
					content: data.content, // 내용
					contentHtml: data.contentHtml, // 내용 html
				};
				boardData.value.place = data.place; // 여행 장소
				boardData.value.status = data.status; // 게시글 상태
				boardData.value.tags = data.tags; // 태그
				boardData.value.title = data.title; // 제목
				boardData.value.travelEndAt = data.travelEndAt; // 여행 종료일
				boardData.value.travelStartAt = data.travelStartAt; // 여행 시작일
				boardData.value.visibility = data.visibility; // 공개 범위
				if (data.featureImgId) {
					featureImgId.value = data.featureImgId; // 대표 이미지
					previewSrc.value = `/web/api/board/file/${featureImgId.value}`; // 대표 이미지 미리보기 url
				}
				extraImgIds.value = data.extraImgIds ?? []; // 추가이미지
			} else {
				// 불러오기 안하면 임시저장 삭제
				deleteDraft(data.boardId);
			}
		})
		.finally(() => {
			endSpinner();
		});
};
onMounted(() => {
	getDraft();
});
</script>
<style scoped>
.board-frame {
	width: 90%;
	justify-self: center;
}
.board-title {
	border-bottom: 2px solid #d1d5db;
}
.board-title:hover {
	border-bottom-color: #10b981; /* 초록색 */
}
.write-area {
	background-color: white;
	border-radius: 15px;
	padding: 30px;
	margin-bottom: 20px;
}
</style>

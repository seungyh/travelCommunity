<template>
	<div class="board-frame">
		<div class="flex justify-between pl-5 pr-5 mt-10 mb-2">
			<div>
				<h2 class="text-2xl font-[700]">새 여행 이야기</h2>
				<span>여행의 소중한 순간을 기록하세요</span>
			</div>
			<div class="flex flex-row">
				<button class="white-btn h-[40px] w-[100px] mr-2">
					<font-awesome-icon :icon="['far', 'floppy-disk']" />
					임시저장
				</button>
				<button
					@click="save"
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
						:tags="[]"
						@updateTags="updateTags"
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
								@click="deleteFeaturedImage"
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
						@change="handleFeatureImgAdded"
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
					<ContentEditor v-model="boardData.content" />
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
						v-for="(src, index) in extraSrcs"
						class="mr-2 mb-2 border-gray"
					>
						<div
							class="w-[250px] h-[200px] text-center content-center relative group"
						>
							<img
								:src="src"
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
						v-if="extraSrcs.length < 8"
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
						@change="handleExtraImgAdded"
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
						@click="boardData.visibility = 'ALL'"
						class="border-gray border-2! hover-green hover:bg-teal-50 cursor-pointer rounded-4xl h-[200px] w-[350px]"
						:class="
							boardData.visibility === 'ALL'
								? 'bg-teal-50 border-green'
								: ''
						"
					>
						<div class="w-full h-full text-center content-center">
							<div>
								<span class="text-gray-400 text-sm"
									><font-awesome-icon :icon="['fas', 'plus']"
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
									><font-awesome-icon :icon="['fas', 'plus']"
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
									><font-awesome-icon :icon="['fas', 'plus']"
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
			<div>
				<span class="cursor-pointer"
					><font-awesome-icon :icon="['fa', 'arrow-left']" />
					돌아가기</span
				>
			</div>
			<div class="flex flex-row">
				<button class="white-btn h-[40px] w-[100px] mr-2">
					<font-awesome-icon :icon="['far', 'floppy-disk']" />
					임시저장
				</button>
				<button
					@click="save"
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
import { ref } from "vue";
import type { BoardRequest } from "./types/request/BoardRequest";
import CustomSelect from "@/components/CustomSelect.vue";
import type { SelectItem } from "@/components/common/types/SelectItem";
import { Korean } from "flatpickr/dist/l10n/ko.js";
import flatPickr from "vue-flatpickr-component";
import TagEditor from "@/components/tiptab/TagEditor.vue";
import ContentEditor from "@/components/tiptab/ContentEditor.vue";
import axios, { AxiosError, type AxiosResponse } from "axios";
import type { CommonResponse } from "@/components/common/types/response/CommonResponse";
import type { ErrorResponse } from "@/components/common/types/response/ErrorResponse";
import router from "@/router";

const previewSrc = ref<string>(""); // 대표 이미지 미리보기
const extraSrcs = ref<string[]>([]); // 추가 이미지
const fileInput = ref<HTMLInputElement | null>(null); // 대표 이미지 input 주소값
const extraFileInput = ref<HTMLInputElement | null>(null); // 추가 이미지 input 주소값
const featureImg = ref<File>(); // 대표 이미지
const extraImgs = ref<File[]>([]); // 추가 이미지
const selectedItem = ref<SelectItem>();
// 작성한 게시글 내용
const boardData = ref<BoardRequest>({
	title: "",
	content: "",
	travelStartAt: null,
	travelEndAt: null,
	category: "",
	tags: [],
	place: "",
	visibility: "ALL", // 전체 공개 ALL, 팔로워 공개 FOLLOW, 비공개 PRIVATE
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

// 태그 에디터에서 입력한 태그 가져오기
const updateTags = (tags: string[]) => {
	boardData.value.tags = tags;
};
// 제목 길이 100자 제한
const limitTitleLength = () => {
	boardData.value.title = boardData.value.title.substring(0, 99);
};
const save = () => {
	boardData.value.title = boardData.value.title.trim();
	boardData.value.place = boardData.value.place.trim();
	if (!validCheck()) {
		return;
	}

	const formData = new FormData();
	// 대표 이미지 formData에 넣기
	if (featureImg.value) {
		formData.append("featureImg", featureImg.value);
	}
	// 추가 이미지 formData에 넣기
	if (extraImgs.value) {
		for (const img of extraImgs.value) {
			formData.append("extraImgs", img);
		}
	}
	// json으로 변환 후 type을 application/json으로 지정하여 formData에 넣기
	const jsonBlob = new Blob([JSON.stringify(boardData.value)], {
		type: "application/json",
	});
	formData.append("boardData", jsonBlob);

	axios
		.post("/web/api/board/write", formData, {
			headers: { "Content-Type": "multipart/form-data" },
		})
		.then((res: AxiosResponse<CommonResponse>) => {
			alert(res.data.message);
			router.push("/");
		})
		.catch((error: AxiosError<ErrorResponse>) => {
			console.log(error);
			alert(error.response?.data.errorMessage);
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
	const tmpContent = boardData.value.content.replace("<p></p>", "");
	if (!tmpContent) {
		alert("내용을 입력해주세요.");
		return false;
	}
	if (!featureImg.value) {
		alert("대표 이미지를 선택해주세요.");
		return false;
	}
	return true;
};
// 추가 이미지 추가
const handleExtraImgAdded = (event: Event) => {
	// 최대 8개로 제한
	if (extraImgs.value && extraImgs.value?.length >= 8) {
		return;
	}
	const target = event.target as HTMLInputElement; // 선택한 파일 가져오기
	const file = target.files?.[0];
	if (file) {
		extraSrcs.value.push(URL.createObjectURL(file));
		extraImgs.value?.push(file);
	}
};
const deleteExtraImage = (index: number) => {
	if (extraImgs.value && extraImgs.value[index]) {
		extraImgs.value.splice(index, 1); // 저장된 파일 정보 삭제
	}
	releaseExtraImgMemory(index);
};
// 대표 이미지 등록
const handleFeatureImgAdded = (event: Event) => {
	const target = event.target as HTMLInputElement; // 선택한 파일 가져오기
	const file = target.files?.[0];

	// 대표 이미지 미리보기 메모리 해제
	releaseFeatureImgMemory();

	if (file) {
		previewSrc.value = URL.createObjectURL(file);
		featureImg.value = file;
	}
};
// 추가 이미지 미리보기 메모리 해제
const releaseExtraImgMemory = (index: number) => {
	if (extraSrcs.value[index]) {
		URL.revokeObjectURL(extraSrcs.value[index]); // url 미리보기 메모리 해제
		extraSrcs.value.splice(index, 1); // 미리보기 url 초기화
	}
};
const openExtraImageFileExplorer = () => {
	extraFileInput.value?.click();
};
// 대표 이미지 파일 탐색기 열기
const openFeaturedImageFileExplorer = () => {
	fileInput.value?.click();
};

// 대표 이미지 삭제
const deleteFeaturedImage = () => {
	if (fileInput.value) {
		fileInput.value.value = ""; // 저장된 파일 정보 삭제
	}
	releaseFeatureImgMemory();
};
// 대표 이미지 미리보기 메모리 해제
const releaseFeatureImgMemory = () => {
	if (previewSrc.value) {
		URL.revokeObjectURL(previewSrc.value); // url 미리보기 메모리 해제
		previewSrc.value = ""; // 미리보기 url 초기화
	}
};
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

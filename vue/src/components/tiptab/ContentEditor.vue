<template>
	<div class="h-full">
		<div className="toolbar">
			<button
				@click="editor?.chain().focus().toggleBold().run()"
				class="toolbar-btn"
				:class="editor?.isActive('bold') ? 'is-active' : ''"
			>
				<b>B</b>
			</button>
			<button
				@click="editor?.chain().focus().toggleItalic().run()"
				class="toolbar-btn"
				:class="editor?.isActive('italic') ? 'is-active' : ''"
			>
				<i>I</i>
			</button>
			<button
				@click="editor?.chain().focus().toggleUnderline().run()"
				class="toolbar-btn"
				:class="editor?.isActive('underline') ? 'is-active' : ''"
				style="text-decoration: underline"
			>
				U
			</button>
			<button
				@click="editor?.chain().focus().toggleStrike().run()"
				:class="editor?.isActive('strike') ? 'is-active' : ''"
				class="toolbar-btn"
				style="text-decoration: line-through"
			>
				S
			</button>

			<div class="toolbar-divider"></div>

			<div class="font-size-dropdown">
				<button
					class="font-size-trigger"
					@click="isFontSizeMenuOpen = !isFontSizeMenuOpen"
					@click.stop
				>
					{{ currentFontSize }}
				</button>

				<!-- 목록 박스 (isOpen이 true일 때만 보이기) -->
				<div
					class="font-size-menu"
					:style="{ display: isFontSizeMenuOpen ? 'block' : 'none' }"
				>
					<!-- 워드 표준 글꼴 크기 배열 -->
					<button
						v-for="size in [
							9, 10, 11, 12, 14, 16, 18, 24, 28, 32, 36,
						]"
						:key="size"
						@click="changeFontSize(size)"
						class="font-size-item"
						:class="{
							'is-active': editor?.isActive('textStyle', {
								currentFontSize: size + 'px',
							}),
						}"
					>
						{{ size }}
					</button>
				</div>
			</div>
			<button
				@click="
					editor?.chain().focus().toggleHeading({ level: 1 }).run()
				"
				:class="
					editor?.isActive('heading', { level: 1 }) ? 'is-active' : ''
				"
				class="toolbar-btn"
			>
				H1
			</button>
			<button
				@click="
					editor?.chain().focus().toggleHeading({ level: 2 }).run()
				"
				:class="
					editor?.isActive('heading', { level: 2 }) ? 'is-active' : ''
				"
				class="toolbar-btn"
			>
				H2
			</button>
			<button
				@click="
					editor?.chain().focus().toggleHeading({ level: 3 }).run()
				"
				:class="
					editor?.isActive('heading', { level: 3 }) ? 'is-active' : ''
				"
				class="toolbar-btn"
			>
				H3
			</button>

			<div class="toolbar-divider"></div>

			<!-- 그룹 3: 정렬 기능 -->
			<button
				@click="editor?.chain().focus().setTextAlign('left').run()"
				:class="
					editor?.isActive({ textAlign: 'left' }) ? 'is-active' : ''
				"
				class="toolbar-btn"
			>
				좌측
			</button>
			<button
				@click="editor?.chain().focus().setTextAlign('center').run()"
				:class="
					editor?.isActive({ textAlign: 'center' }) ? 'is-active' : ''
				"
				class="toolbar-btn"
			>
				중앙
			</button>
			<button
				@click="editor?.chain().focus().setTextAlign('right').run()"
				:class="
					editor?.isActive({ textAlign: 'right' }) ? 'is-active' : ''
				"
				class="toolbar-btn"
			>
				우측
			</button>

			<div class="toolbar-divider"></div>

			<!-- 리스트 & 인용구 -->
			<button
				@click="editor?.chain().focus().toggleBulletList().run()"
				:class="editor?.isActive('bulletList') ? 'is-active' : ''"
				class="toolbar-btn"
			>
				•
			</button>
			<button
				@click="editor?.chain().focus().toggleOrderedList().run()"
				:class="editor?.isActive('orderedList') ? 'is-active' : ''"
				class="toolbar-btn"
			>
				1.
			</button>
			<button
				@click="editor?.chain().focus().toggleBlockquote().run()"
				:class="editor?.isActive('blockquote') ? 'is-active' : ''"
				class="toolbar-btn"
			>
				”
			</button>

			<div class="toolbar-divider"></div>

			<!-- 색상 및 컴포넌트 삽입 -->
			<input
				type="color"
				class="toolbar-color-picker"
				@click="setColor"
			/>
			<button class="toolbar-btn" @click="openFileExplorer">
				<i class="bi bi-image"></i>
			</button>
			<input
				ref="fileInput"
				type="file"
				hidden
				accept="image/*"
				@change="onFileChange"
			/>
			<button
				@click="editor?.chain().focus().toggleCodeBlock().run()"
				:class="editor?.isActive('codeBlock') ? 'is-active' : ''"
				class="toolbar-btn"
			>
				&lt;/&gt;
			</button>
		</div>
		<EditorContent :editor="editor" />
	</div>
</template>
<script setup lang="ts">
import { nextTick, onBeforeUnmount, onMounted, ref, watch } from "vue";
import StarterKit from "@tiptap/starter-kit";
import { EditorContent, useEditor } from "@tiptap/vue-3";
import TextAlign from "@tiptap/extension-text-align";
import { ListItem } from "@tiptap/extension-list";
import { Color, FontSize, TextStyle } from "@tiptap/extension-text-style";
import FileHandler from "@tiptap/extension-file-handler";
import Image from "@tiptap/extension-image";
import { Dropcursor } from "@tiptap/extensions";
import type { ContentEditorModelValue } from "@/pages/board/write/types/ContentEditorModelValue";
import axios, { AxiosError, type AxiosResponse } from "axios";
import type { ErrorResponse } from "../common/types/response/ErrorResponse";
import { useSpinnerStore } from "@/stores/Spinner";

const props = defineProps(["modelValue"]);
const emit = defineEmits<{
	(e: "update:modelValue", content: ContentEditorModelValue): void;
	(e: "draft"): void;
}>();
const { startSpinner, endSpinner } = useSpinnerStore();

const currentFontSize = ref<number>(10);
const isFontSizeMenuOpen = ref(false);
const fileInput = ref<HTMLInputElement | null>();
const editor = useEditor({
	content: "<p></p>",

	extensions: [
		StarterKit,
		TextAlign.configure({
			types: ["heading", "paragraph"],
		}),
		FontSize,
		TextStyle,
		Color.configure({ types: [TextStyle.name, ListItem.name] }),
		Image.configure({
			// 내장 리사이즈 기능 활성화 (상, 하, 좌, 우, 대각선 핸들 자동 생성)
			resize: {
				enabled: true,
				alwaysPreserveAspectRatio: true,
			},
		}),
		Dropcursor,
		FileHandler.configure({
			allowedMimeTypes: [
				"image/png",
				"image/jpeg",
				"image/gif",
				"image/webp",
			],
			onDrop: async (currentEditor, files, pos) => {
				startSpinner();
				for (const file of files) {
					const src = await uploadImg(file);
					if (src) {
						currentEditor
							.chain()
							.insertContentAt(pos, {
								type: "image",
								attrs: {
									src: src,
								},
							})
							.focus()
							.run();
						emit("draft");
					}
				}
				endSpinner();
			},
			onPaste: async (currentEditor, files) => {
				startSpinner();
				for (const file of files) {
					const src = await uploadImg(file);
					if (src) {
						currentEditor
							.chain()
							.insertContentAt(
								currentEditor.state.selection.anchor,
								{
									type: "image",
									attrs: {
										src: src,
									},
								},
							)
							.focus()
							.run();
						emit("draft");
					}
				}
				endSpinner();
			},
		}),
	],
	onUpdate({ editor }) {
		const modelValue = {
			contentHtml: editor.getHTML(),
			content: editor.getText(),
		};
		emit("update:modelValue", modelValue);
	},
	async onDelete(props) {
		if (props.type !== "node") return;
		if (props.node.type.name !== "image") return;

		const src = props.node.attrs.src as string;

		let exists = false;

		props.editor.state.doc.descendants((node) => {
			if (node.type.name === "image" && node.attrs.src === src) {
				exists = true;
			}
		});

		// 리사이즈 등으로 기존 노드가 교체된 경우
		if (exists) {
			return;
		}

		// 삭제한 이미지 크기
		const width = props.node.attrs.width;
		const height = props.node.attrs.height;

		// 서버에서 이미지 삭제
		const res = await deleteImage(src);

		// 서버에서 이미지 삭제 실패, 에디터에 이미지 재생성
		if (!res) {
			const deletedPos = props.from;
			// 서버에서 삭제 안됐으면 다시 넣기
			editor.value
				?.chain()
				.focus()
				.insertContentAt(deletedPos, {
					type: "image",
					attrs: {
						src: props.node.attrs.src,
						height,
						width,
					},
				})
				.run();
		}
		emit("draft");
	},
	editorProps: {
		attributes: {
			style: "min-height: 500px; outline: none; padding: 16px;",
		},
	},
});

// 폰트 사이즈 변경
const changeFontSize = (size: number) => {
	editor?.value
		?.chain()
		.focus()
		.setFontSize(size + "px")
		.run();
	currentFontSize.value = size;
};
// 이미지 추가 버튼 클릭
const openFileExplorer = () => {
	fileInput.value?.click();
};
// 이미지 선택해서 추가
const onFileChange = async (event: Event) => {
	const input = event.target as HTMLInputElement;
	const file = input.files?.[0];

	if (!file || !editor.value) return;
	const src = await uploadImg(file);

	if (src) {
		editor.value?.chain().focus().setImage({ src }).run();
	}
};

const uploadImg = async (file: File) => {
	const MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

	if (file && file.size > MAX_FILE_SIZE) {
		alert("파일 크기는 5MB 이하만 업로드할 수 있습니다.");
		return;
	}

	const formData = new FormData();
	if (file) {
		formData.append("file", file);
	}
	const jsonBlob = new Blob([JSON.stringify("CONTENT")], {
		type: "application/json",
	});
	formData.append("type", jsonBlob);
	// 파일 업로드
	return axios
		.post("/web/api/board/file/upload", formData, {
			headers: { "Content-Type": "multipart/form-data" },
		})
		.then((res: AxiosResponse<number>) => {
			return `/web/api/board/file/${res.data}`;
		})
		.catch((res: AxiosError<ErrorResponse>) => {
			alert("파일 업로드 실패하였습니다.");
			return null;
		});
};

// 파일 삭제 서버에서 삭제 실패하면 false 반환
const deleteImage = async (url: string) => {
	startSpinner();
	return await axios
		.delete(url)
		.then((res: AxiosResponse<number>) => {
			return true;
		})
		.catch((res: AxiosError<ErrorResponse>) => {
			alert("파일 삭제 실패하였습니다.");
			return false;
		})
		.finally(() => {
			endSpinner();
		});
};

const setColor = (event: Event) => {
	const target = event.target as HTMLInputElement;
	editor?.value?.chain().focus().setColor(target.value);
};
const handleBodyClick = () => {
	isFontSizeMenuOpen.value = false;
};

watch(
	() => props.modelValue,
	(value) => {
		editor?.value
			?.chain()
			.focus()
			.setContent(props.modelValue.contentHtml)
			.run();
	},
	{ immediate: true },
);
onMounted(async () => {
	await nextTick();
	window.addEventListener("click", handleBodyClick);
	changeFontSize(14);
});
onBeforeUnmount(() => {
	editor.value?.destroy();
});
</script>

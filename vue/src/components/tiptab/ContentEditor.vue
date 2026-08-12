<template>
	<div class="h-full">
		<div className="toolbar">
			<!-- <button
				@click="editor?.chain().focus().toggleBold().run()"
				class="bold"
				:class="editor?.isActive('bold') ? 'is-active' : ''"
			>
				B
			</button>
			<button
				@click="editor?.chain().focus().toggleItalic().run()"
				class="italic"
				:class="editor?.isActive('italic') ? 'is-active' : ''"
			>
				I
			</button>
			<button
				@click="editor?.chain().focus().toggleStrike().run()"
				class="line-through"
				:class="editor?.isActive('strike') ? 'is-active' : ''"
			>
				S
			</button> -->
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
import { nextTick, onBeforeUnmount, onMounted, ref } from "vue";
import StarterKit from "@tiptap/starter-kit";
import { EditorContent, useEditor } from "@tiptap/vue-3";
import TextAlign from "@tiptap/extension-text-align";
import { ListItem } from "@tiptap/extension-list";
import { Color, FontSize, TextStyle } from "@tiptap/extension-text-style";
import FileHandler from "@tiptap/extension-file-handler";
import Image from "@tiptap/extension-image";
import { Dropcursor } from "@tiptap/extensions";

const emit = defineEmits<{ (e: "update:modelValue", content: string): void }>();
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
			allowBase64: true, // 이미지를 base64 문자열로 파싱할 수 있도록 허용
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
			onDrop: (currentEditor, files, pos) => {
				files.forEach((file) => {
					const fileReader = new FileReader();

					fileReader.readAsDataURL(file);
					fileReader.onload = () => {
						currentEditor
							.chain()
							.insertContentAt(pos, {
								type: "image",
								attrs: {
									src: fileReader.result,
								},
							})
							.focus()
							.run();
					};
				});
			},
			onPaste: (currentEditor, files) => {
				files.forEach((file) => {
					const fileReader = new FileReader();

					fileReader.readAsDataURL(file);
					fileReader.onload = () => {
						currentEditor
							.chain()
							.insertContentAt(
								currentEditor.state.selection.anchor,
								{
									type: "image",
									attrs: {
										src: fileReader.result,
									},
								},
							)
							.focus()
							.run();
					};
				});
			},
		}),
	],
	onUpdate({ editor }) {
		emit("update:modelValue", editor.getHTML());
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
const onFileChange = (event: Event) => {
	const input = event.target as HTMLInputElement;
	const file = input.files?.[0];

	if (!file || !editor.value) return;

	const reader = new FileReader();

	reader.onload = () => {
		const src = reader.result as string;

		editor.value?.chain().focus().setImage({ src }).run();
		input.value = "";
	};

	reader.readAsDataURL(file);
};
const setColor = (event: Event) => {
	const target = event.target as HTMLInputElement;
	editor?.value?.chain().focus().setColor(target.value);
};
const handleBodyClick = () => {
	isFontSizeMenuOpen.value = false;
};
onMounted(async () => {
	await nextTick();
	window.addEventListener("click", handleBodyClick);
	changeFontSize(14);
});
onBeforeUnmount(() => {
	editor.value?.destroy();
});
</script>

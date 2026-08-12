<template>
	<div>
		<EditorContent
			:editor="editor"
			placeholder="태그 입력 후 Enter"
			@keydown="handleKeyDown"
		/>
	</div>
</template>
<script setup lang="ts">
import { nextTick, onBeforeUnmount, onMounted } from "vue";
import StarterKit from "@tiptap/starter-kit";
import { EditorContent, useEditor } from "@tiptap/vue-3";

import { TagNode } from "./node/TagNode";

const props = defineProps<{ tags: [] }>(); // 기존에 입력한 태그
const emit = defineEmits<{ (e: "updateTags", item: string[]): void }>();

const editor = useEditor({
	content: "<p></p>",

	extensions: [StarterKit, TagNode],

	// 에디터 변경 시 부모 컴포넌트로 태그 전송
	onUpdate({ editor }) {
		emit("updateTags", getTags());
	},
	editorProps: {
		attributes: {
			class: "tag-editor",
		},
		// enter는 줄바꿈 안함 tiptab 내부 설정
		// 일반적인 dom 엔터 preventDefault는 안먹음
		handleKeyDown(view, event) {
			if (event.key === "Enter") {
				// true를 반환하면 ProseMirror 기본 Enter 동작 중단
				return true;
			}

			return false;
		},
	},
});

const handleKeyDown = (event: KeyboardEvent) => {
	if (event.key !== "Enter") {
		return;
	}

	addTag();
};
// 입력한 문자열을 태그 노드로 추가
const addTag = () => {
	if (!editor.value) {
		return;
	}

	const { state } = editor.value;
	const { $from } = state.selection;

	// 현재 커서 바로 앞 노드 가져오기
	const nodeBefore = $from.nodeBefore;

	// 입력한 텍스트가 아니면 처리 안함
	if (!nodeBefore?.isText) {
		return;
	}
	const value = nodeBefore.text ?? "";

	if (!tagValidCheck(value)) {
		return;
	}
	const from = $from.pos - value.length;
	const to = $from.pos;

	// 사용자가 입력한 값 노드 생성하여 editor에 넣기
	editor.value
		.chain()
		.focus()
		.deleteRange({
			from,
			to,
		})
		.insertContent({
			type: "tag",
			attrs: {
				value,
			},
		})
		.run();
};

const tagValidCheck = (inputTag: string) => {
	const tags: string[] = getTags();
	// 동일 해시 등록 불가
	if (tags.includes(inputTag)) {
		alert("이미 등록한 해시 태그입니다.");
		return false;
	}
	// 해시는 10개까지
	if (tags.length >= 10) {
		alert("해시 태그는 10개까지 등록 가능합니다.");
		return false;
	}
	return true;
};

// 현재까지 등록한 태그 값 모두 가져오기
const getTags = () => {
	const tags: string[] = [];

	// proseMirror에서 내부적으로 순회
	editor.value?.state.doc.descendants((node) => {
		if (node.type.name === "tag" && node.attrs.value) {
			tags.push(node.attrs.value);
		}
	});
	return tags;
};

// 기존에 입력한 태그 값 태그 chip으로 생성
const makeHashTagChips = async () => {
	await nextTick();
	if (!editor.value) {
		return;
	}
	for (const tag of props.tags) {
		editor.value
			.chain()
			.focus()
			.insertContent({
				type: "tag",
				attrs: {
					value: tag,
				},
			})
			.run();
	}
};
onMounted(() => {
	makeHashTagChips();
});
onBeforeUnmount(() => {
	editor.value?.destroy();
});
</script>

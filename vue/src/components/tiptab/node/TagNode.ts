import { Node, mergeAttributes } from "@tiptap/core";
import { VueNodeViewRenderer } from "@tiptap/vue-3";

import TagNodeView from "./TagNodeView.vue";

export const TagNode = Node.create({
	// insertContent에서 사용할 노드 이름
	name: "tag",

	// 문장 안에 들어가는 inline 노드
	group: "inline",
	inline: true,

	// 하나의 덩어리처럼 동작
	atom: true,

	// 태그 데이터 정의
	addAttributes() {
		return {
			value: {
				default: "",
			},
		};
	},

	// 저장된 HTML을 다시 Tiptap으로 불러올 때 사용
	parseHTML() {
		return [
			{
				tag: 'span[data-type="tag"]',
			},
		];
	},

	// editor.getHTML() 호출 시 만들어지는 HTML
	renderHTML({ HTMLAttributes }) {
		return [
			"span",
			mergeAttributes(HTMLAttributes, {
				"data-type": "tag",
				"data-value": HTMLAttributes.value,
				class: "tag-node",
			}),
			`#${HTMLAttributes.value}`,
		];
	},

	// 에디터 화면에서는 Vue 컴포넌트로 렌더링
	addNodeView() {
		return VueNodeViewRenderer(TagNodeView);
	},
});

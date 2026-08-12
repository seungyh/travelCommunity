<template>
	<NodeViewWrapper as="span" class="tag-chip">
		<span class="tag-text">#{{ props.node.attrs.value }}</span>

		<font-awesome-icon
			class="tag-remove-button"
			@click="removeTag"
			:icon="['fas', 'xmark']"
		/>
	</NodeViewWrapper>
</template>
<script setup lang="ts">
import { NodeViewWrapper, type NodeViewProps } from "@tiptap/vue-3";

const props = defineProps<NodeViewProps>();

const removeTag = () => {
	props.deleteNode();
};
</script>

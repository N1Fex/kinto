<script lang="ts">
	import type { LucideProps } from '@lucide/svelte';

	let {
		size = 24,
		width,
		height,
		stroke = 'currentColor',
		strokeWidth = 0,
		class: className = '',
		title = 'Kinto',
		ariaHidden = false
	} = $props<LucideProps & {
		fill?: string;
		title?: string;
		ariaHidden?: boolean;
	}>();

	let resolvedWidth = $derived(width ?? size);
	let resolvedHeight = $derived(height ?? size);

</script>

<svg
	width={resolvedWidth}
	height={resolvedHeight}
	viewBox="0 0 24 24"
	class={className}
	stroke={stroke}
	stroke-width={strokeWidth}
	role={ariaHidden ? undefined : 'img'}
	aria-label={ariaHidden ? undefined : title}
	aria-hidden={ariaHidden}
	xmlns="http://www.w3.org/2000/svg"
>
	{#if !ariaHidden}<title>{title}</title>{/if}
	
	<rect x="4" y="7" width="4" height="10"
		fill="var(--logo-line)" />
	
	<line x1="8" y1="12" x2="18" y2="5" stroke="var(--logo-line)" stroke-width="4" />
	<line x1="8" y1="12" x2="18" y2="19" stroke="var(--logo-line)" stroke-width="4" />


	<circle cx="6" cy="5" r="4" fill="var(--logo-c1)" />
	<circle cx="6" cy="19" r="4" fill="var(--logo-c1)" />
	<circle cx="18" cy="5" r="4" fill="var(--logo-c2)" />
	<circle cx="18" cy="19" r="4" fill="var(--logo-c2)" />
</svg>

<style>
</style>

<script lang="ts">
	import type { LucideProps } from '@lucide/svelte';

	let {
		size = 24,
		width,
		height,
		stroke = 'currentColor',
		strokeWidth = 2,
		fill = 'none',
		strokeLinecap = 'round',
		strokeLinejoin = 'round',
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
	fill={fill}
	stroke={stroke}
	stroke-width={strokeWidth}
	stroke-linecap={strokeLinecap}
	stroke-linejoin={strokeLinejoin}
	role={ariaHidden ? undefined : 'img'}
	aria-label={ariaHidden ? undefined : title}
	aria-hidden={ariaHidden}
	xmlns="http://www.w3.org/2000/svg"
>
	{#if !ariaHidden}<title>{title}</title>{/if}

	<circle cx="6" cy="5" r="3" fill="transparent"/>
	<circle cx="6" cy="19" r="3" fill="transparent"/>
	<circle cx="18" cy="5" r="3" fill="transparent"/>
	<circle cx="18" cy="19" r="3" fill="transparent"/>


	<line x1="4" y1="7" x2="4" y2="17" />
	

	<path d="M 8 16.70871215252208 
			L 8 13.7
			L 15 18.4"
		fill="none"
		stroke-linejoin="round"
		stroke-linecap="round"/>

	<path d="M 16.40878 15.87179 
			L 11 12
			L 16.5 8"
		fill="none"
		stroke-linejoin="round"
		stroke-linecap="round" />

	<path d="M 8 7.5
			L 8 10.3
			L 14.7 5.6"
		fill="none"
		stroke-linejoin="round" 
		stroke-linecap="round"/>
</svg>
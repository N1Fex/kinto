<script lang="ts">
	let { src = null, name = '?', size = 40 } = $props<{
		src?: string | null;
		name?: string;
		size?: number;
	}>();

	let initials = $derived(name
		.trim()
		.split(/\s+/)
		.slice(0, 2)
		.map((word: string) => word[0]?.toUpperCase())
		.join(''));
</script>

<div class="avatar" style="width:{size}px;height:{size}px;font-size:{size * 0.4}px">
	{#if src}
		<img {src} alt={name} width={size} height={size} style="object-fit:cover;width:100%;height:100%" />
	{:else}
		{initials}
	{/if}
</div>
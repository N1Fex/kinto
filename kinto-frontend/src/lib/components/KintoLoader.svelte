<!-- lib/components/KintoLoader.svelte -->
<script lang="ts">
	let { size = 64, label = 'Загрузка...' } = $props<{
		size?: number;
		label?: string;
	}>();
</script>

<div class="loader-wrap">
	<svg
		width={size}
		height={size}
		viewBox="0 0 24 24"
		xmlns="http://www.w3.org/2000/svg"
		role="img"
		aria-label="Загрузка"
	>
		<line x1="8" y1="12" x2="18" y2="5" stroke="var(--logo-line)" stroke-width="4" />
		<line x1="8" y1="12" x2="18" y2="19" stroke="var(--logo-line)" stroke-width="4" />
		<rect x="4" y="7" width="4" height="10" fill="var(--logo-line)" />

		<circle class="node n1" cx="6" cy="5" r="4" fill="var(--logo-c1)" />
		<circle class="node n2" cx="6" cy="19" r="4" fill="var(--logo-c1)" />
		<circle class="node n3" cx="18" cy="5" r="4" fill="var(--logo-c2)" />
		<circle class="node n4" cx="18" cy="19" r="4" fill="var(--logo-c2)" />
	</svg>
	{#if label}<span class="loader-label">{label}</span>{/if}
</div>

<style>
	.loader-wrap {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 0.75rem;
	}

	.node {
		transform-origin: center;
		transform-box: fill-box;
		animation: pulse 1.4s ease-in-out infinite;
	}

	.n1 { animation-delay: 0s; }
	.n3 { animation-delay: 0.15s; }
	.n4 { animation-delay: 0.3s; }
	.n2 { animation-delay: 0.45s; }

	@keyframes pulse {
		0%, 100% {
			transform: scale(1);
			opacity: 1;
		}
		50% {
			transform: scale(0.55);
			opacity: 0.45;
		}
	}

	.loader-label {
		font-size: 0.85rem;
		font-weight: 600;
		color: var(--color-text-muted);
	}

	@media (prefers-reduced-motion: reduce) {
		.node {
			animation: none;
		}
	}
</style>
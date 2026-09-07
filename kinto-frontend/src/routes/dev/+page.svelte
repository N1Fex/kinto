<script lang="ts">
	import KintoLoader from "$lib/components/KintoLoader.svelte";
</script>


<div class="app-loading">
    <KintoLoader />
</div>

<style>
    .app-loading {
		min-height: 100vh;
		display: flex;
		align-items: center;
		justify-content: center;
		background: var(--color-bg);
	}
</style>
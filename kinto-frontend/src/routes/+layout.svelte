<script lang="ts">
	import Header from "$lib/components/Header.svelte";
	import Sidebar from "$lib/components/Sidebar.svelte";
	import Footer from "$lib/components/Footer.svelte";
	import KintoLoader from "$lib/components/KintoLoader.svelte";
	import { initializeTheme } from "$lib/theme";
	import { user } from "$lib/stores/userStore";
	import { page } from "$app/state";
	import "../app.css";
	import { onMount } from 'svelte';
 	import { initialRefreshTokens } from '$lib/httpClient';
	
	let isInitializing = $state(true);
	let showLoader = $state(false);

	let { children } = $props();

	initializeTheme();

	onMount(async () => {
		const loaderTimer = setTimeout(() => {
			showLoader = true; // покажем лоадер, только если рефреш занял больше 150мс
		}, 150);

		await initialRefreshTokens();

		clearTimeout(loaderTimer);
		isInitializing = false;
		showLoader = false;
	});
</script>

{#if isInitializing}
	{#if showLoader}
		<div class="app-loading">
			<KintoLoader />
		</div>
	{/if}
{:else}

	<div class="shell">
		{#if !$user && page.url.pathname !== "/"}<Header />{/if}
		<div
			class:with-sidebar={$user}
			class:messages-layout={$user && page.url.pathname === "/messages"}
			class:home-layout={!$user && page.url.pathname === "/"}
			class="page-layout"
		>
			<Sidebar />
			<main>{@render children()}</main>
		</div>
		{#if !($user && page.url.pathname === "/messages")}<Footer />{/if}
	</div>
{/if}

<style>
	.shell {
		display: flex;
		min-height: 100vh;
		flex-direction: column;
	}

	.page-layout {
		width: min(100% - 2rem, 1120px);
		margin: 0 auto;
		padding: 2rem 0 3rem;
	}
	.page-layout.home-layout {
		flex: 1;
		display: flex;
		align-items: center;
	}
	.page-layout.home-layout main,
	.page-layout:not(.with-sidebar) main {
		width: 100%;
	}
	.page-layout.with-sidebar {
		width: 100%;
		max-width: none;
		margin: 0;
		padding: 2.5rem 2rem 3rem 6rem;
	}
	.page-layout.with-sidebar main {
		width: min(100%, 1200px);
		margin: 0 auto;
	}
	.page-layout.messages-layout {
		width: 100%;
		padding: 0 0 0 72px;
	}
	.page-layout.messages-layout main {
		width: 100%;
		max-width: none;
		margin: 0;
	}
	.app-loading {
		min-height: 100vh;
		display: flex;
		align-items: center;
		justify-content: center;
		background: var(--color-bg);
	}
	@media (max-width: 760px) {
		.page-layout.with-sidebar {
			width: min(100% - 2rem, 680px);
			padding: 2rem 0 3rem;
		}
		.page-layout.messages-layout {
			width: 100%;
			padding: 0;
		}
	}
</style>

<script lang="ts">
	import { page } from '$app/state';
    import { User, Shield, MonitorSmartphone } from '@lucide/svelte';

	const navItems = [
		{ href: '/settings/account', label: 'Аккаунт', icon: User },
		{ href: '/settings/privacy', label: 'Конфиденциальность', icon: Shield },
		{ href: '/settings/sessions', label: 'Активные сеансы', icon: MonitorSmartphone },
	]   

	let { children } = $props();
</script>

<svelte:head><title>Настройки — Kinto</title></svelte:head>

<div class="settings-layout">
	<nav class="settings-nav card">
		{#each navItems as item}
			<a
				href={item.href}
				class="nav-item"
				class:active={page.url.pathname === item.href}
			>
				<span class="icon">
					<item.icon size={18} />
				</span>
				{item.label}
			</a>
		{/each}
	</nav>

	<section class="settings-content card card-pad">
		{@render children()}
	</section>
</div>

<style>
	.settings-layout {
		display: grid;
		grid-template-columns: 240px 1fr;
		gap: 1.25rem;
		align-items: start;
	}

	.settings-nav {
		display: flex;
		flex-direction: column;
		gap: 0.15rem;
		padding: 0.5rem;
		position: sticky;
		top: 2rem;
	}

	.nav-item {
		display: flex;
		align-items: center;
		gap: 0.65rem;
		padding: 0.6rem 0.75rem;
		border-radius: var(--radius-md);
		color: var(--color-text-muted);
		font-size: 0.9rem;
		font-weight: 600;
	}

	.nav-item:hover {
		background: var(--color-surface-hover);
		color: var(--color-text);
	}

	.nav-item.active {
		background: var(--color-primary-soft);
		color: var(--color-primary);
	}

	.icon {
		display: flex;
		width: 18px;
		height: 18px;
		flex: none;
	}

	@media (max-width: 760px) {
		.settings-layout {
			grid-template-columns: 1fr;
		}

		.settings-nav {
			flex-direction: row;
			overflow-x: auto;
			position: static;
			gap: 0.35rem;
		}

		.nav-item {
			flex: none;
			white-space: nowrap;
		}
	}
</style>
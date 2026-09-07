<script lang="ts">
	import { page } from "$app/state";
	import { House, UserRound, MessageCircle, SearchIcon, BellIcon, SettingsIcon } from '@lucide/svelte';
	import { user } from "$lib/stores/userStore";
    import KintoLogotype from "./KintoLogotype.svelte";
    import KintoLogotypeGlyph from "./KintoLogotypeGlyph.svelte";
    import ThemeButton from "./ThemeButton.svelte";
	
	const links = [
		{ href: "/", label: "Главная", icon: House },
		{ href: "/messages", label: "Чаты", icon: MessageCircle },
		{ href: "/search", label: "Поиск", icon: SearchIcon },
		{ href: "/notifications", label: "Уведомления", icon: BellIcon },
		{ href: "/profile", label: "Профиль", icon: UserRound },
	];
</script>

{#if $user}
	<aside class="side-rail">
		<nav>
			<a
				href={"/"}
				title={"Kinto"}
				class="logo"
			>
				<div style="width: 32px; height: 32px">
					<KintoLogotypeGlyph class="glyph" size={32} strokeWidth={1.75} />
					<KintoLogotype class="logo" size={32}/>
				</div>
				
				<span class="label">Kinto</span>
			</a>
		</nav>

		<nav aria-label="Основная навигация">
			{#each links as link}
				{@const Icon = link.icon}
				<a
					class:active={page.url.pathname === link.href}
					href={link.href}
					title={link.label}
				>
					<Icon class="icon" size={24} strokeWidth={1.8} aria-hidden="true" />
					<span class="label">{link.label}</span>
				</a>
			{/each}
		</nav>
		<nav>
			<ThemeButton className="theme-button">
				<span class="label">Сменить тему</span>
			</ThemeButton>

			<a
				class:active={page.url.pathname === "/settings"}
				href={"/settings"}
				title={"Настройки"}
			>
				<SettingsIcon class="icon" size={24} strokeWidth={1.8} aria-hidden="true" />
				<span class="label">Настройки</span>
			</a>
		</nav>

	</aside>
{/if}

<style>
	.side-rail {
		position: fixed;
		inset: 0 auto 0 0;
		z-index: 20;
		display: flex;
		flex-direction: column;
		width: 72px;
		align-items: center;
		justify-content: space-between;
		border-right: 1px solid var(--color-border);
		background: var(--color-surface);
		padding: 24px 12px;
		transition:
			width 180ms ease,
			box-shadow 180ms ease;
	}
	.side-rail:hover,
	.side-rail:focus-within {
		width: 200px;
		align-items: stretch;
		box-shadow: 8px 0 24px rgb(0 0 0 / 20%);
	}

	nav {
		display: grid;
		width: 100%;
		gap: 0.35rem;
	}
	nav a {
		display: flex;
		align-items: center;
		border-radius: var(--radius-md);
		color: var(--color-text-muted);
		font-weight: 600;
		padding: 0.8rem;
	}


	.label {
		white-space: nowrap;
		opacity: 0;
		width: 0;
		transition:
			margin 180ms ease,
			opacity 180ms ease;
	}

	nav .logo {
		font-weight: 900;
		letter-spacing: 0.25rem;
		padding: 0.8rem 0.5rem;
		
	}

	nav a:hover:not(.logo),
	nav a.active {
		background: var(--color-primary-soft);
		color: var(--color-primary);
	}

	nav a.logo :global(svg) {
		position: absolute;
		transition:
			opacity 360ms ease;
	}

	nav a.logo span.label {
		transition:
			margin 180ms ease,
			opacity 180ms ease,
			color 360ms ease;
	}

	nav a.logo:hover span.label {
		color: var(--color-primary);
	}

	nav a.logo:hover :global(svg.logo),
	nav a.logo :global(svg.glyph) {
		opacity: 1;
	}

	nav a.logo:hover :global(svg.glyph),
	nav a.logo :global(svg.logo) {
		opacity: 0;
	}

	nav a.active {
		font-weight: bold;

	}

	nav a.active :global(.icon) {
		stroke-width: 3px;
	}

	:global(.icon) {
		display: grid;
		flex: none;
		place-items: center;
		color: currentColor;
		line-height: 1;
	}

	.side-rail:hover .label,
	.side-rail:focus-within .label {
		opacity: 1;
		margin-left: 20px;
	}
	@media (max-width: 760px) {
		.side-rail {
			display: none;
		}
	}
</style>

<script lang="ts">
	import { user, } from "$lib/stores/userStore";
	import ThemeButton from "./ThemeButton.svelte";
	import Avatar from "$lib/components/ui/Avatar.svelte";
	import KintoLogotype from "$lib/components/KintoLogotype.svelte";
</script>

<header>
	<div class="header-inner">
		<a class="brand" href="/" aria-label="Kinto">
			<KintoLogotype
				class="brand-logo"
				width={48}
				height={48}
				title="Kinto"
			/>
			<span class="brand-name">Kinto</span>
		</a>
		<div class="actions">
			{#if $user}
			<a class="profile" href="/profile"
					><Avatar
						src={$user.avatarUrl}
						name={$user.name}
						size={28}
					/>
					<span class="profile-name">{$user.name}</span>
				</a>
				{:else}
				<a class="btn btn-primary btn-sm" href="/login">Войти</a>
				<a class="btn btn-secondary btn-sm" href="/register"
					>Зарегистрироваться</a
				>
				{/if}
			</div>
			<ThemeButton style="position: absolute; top: 12px; right: 12px"/>
		</div>
	</header>

<style>
	header {
		position: sticky;
		top: 0;
		z-index: 10;
		border-bottom: 1px solid var(--color-border);
		background: color-mix(in srgb, var(--color-surface) 92%, transparent);
		backdrop-filter: blur(10px);
	}
	.header-inner {
		display: flex;
		justify-content: space-between;
		align-items: center;
		gap: 1rem;
		width: min(100% - 2rem, 1120px);
		min-height: 4.5rem;
		margin: 0 auto;
	}
	.brand {
		display: inline-flex;
		align-items: center;
		gap: 0.55rem;
		font-size: 1rem;
		font-weight: 750;
		letter-spacing: -0.03em;
	}

	.actions {
		display: flex;
		flex-direction: row;
		align-items: center;
		gap: 12px;
	}

	@media (max-width: 620px) {
		.profile-name {
			display: none;
		}
	}
</style>

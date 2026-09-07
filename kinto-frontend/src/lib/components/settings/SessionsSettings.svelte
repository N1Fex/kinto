<script lang="ts">
	import { onMount } from 'svelte';
	import { apiFetch } from '$lib/httpClient';
    import { Monitor, Smartphone, Tablet, Globe, 
             BadgeAlert } from '@lucide/svelte';

	interface SessionDto {
		userId: number;
		sid: string;
		device: string;
		country: string;
		browser: string;
		os: string;
		ip: string;
		createdAt: string;
		expiresAt: string;
	}

    const deviceIcons: Record<string, typeof Monitor> = {
		Desktop: Monitor,
        Browser: Globe,
		Phone: Smartphone,
		Tablet: Tablet,
	};

	let sessions = $state<SessionDto[]>([]);
	let currentSid = $state<string | null>(null);
	let loading = $state(true);
	let error = $state<string | null>(null);
    let successMessage = $state<string | null>(null);
	let revokingId = $state<string | null>(null);
    

	onMount(loadSessions);
    
    let currentSession = $derived(sessions.find((s) => s.sid === currentSid));
	let otherSessions = $derived(sessions.filter((s) => s.sid !== currentSid));

	let revokingAll = $state(false);


	async function loadSessions() {
		loading = true;
		error = null;
		try {
			const res = await apiFetch('/sessions');
			if (!res.ok) throw new Error('Не удалось загрузить сессии');
			const data = await res.json();
			sessions = data.sessions ?? data;
			currentSid = data.currentSid ?? null;
		} catch (err: any) {
			error = err.message;
		} finally {
			loading = false;
		}
	}

	async function revokeSession(sid: string) {
		revokingId = sid;
		try {
			const res = await apiFetch(`/sessions/${sid}`, { method: 'DELETE' });
			if (!res.ok) throw new Error('Не удалось завершить сессию');
			sessions = sessions.filter((s) => s.sid !== sid);
		} catch (err: any) {
			error = err.message;
		} finally {
			revokingId = null;
		}
	}

    async function revokeAllOtherSessions() {
		revokingAll = true;
		try {
			const res = await apiFetch('/sessions/others', { method: 'DELETE' });
			if (!res.ok) throw new Error('Не удалось завершить сессии');
			sessions = sessions.filter((s) => s.sid === currentSid);
            successMessage = "Все сессии успешно завершены"
		} catch (err: any) {
			error = err.message;
		} finally {
			revokingAll = false;
		}
	}

	function formatDate(iso: string) {
		return new Date(iso).toLocaleString('ru-RU', {
			day: '2-digit', month: '2-digit', year: 'numeric',
			hour: '2-digit', minute: '2-digit',
		});
	}
</script>

<h2 class="h2">Активные сеансы</h2>
<p class="text-subtle">Устройства, с которых выполнен вход в аккаунт</p>

{#if loading}
	<p class="text-muted">Загрузка...</p>
{:else if error}
	<p class="error">{error}</p>
{:else if sessions.length === 0}
	<p class="text-muted">Нет активных сеансов</p>
{:else}
	{#if currentSession}
		{@const Icon = deviceIcons[currentSession.device] ?? Globe}
		<h3 class="h3 section-title">Это устройство</h3>
		<div class="session-item current">
			<div class="session-icon">
				<Icon size={20} />
			</div>
			<div class="session-info">
				<div class="session-title">
					{currentSession.browser} · {currentSession.os}
					<span class="badge">Текущий</span>
				</div>
				<div class="text-subtle">{currentSession.device} · {currentSession.country}</div>
				<div class="text-subtle">Последняя активность: {formatDate(currentSession.createdAt)}</div>
			</div>
			<button
				class="btn btn-sm revoke-filled"
				onclick={() => revokeSession(currentSession.sid)}
			>
				Завершить
			</button>
		</div>

		<button
			class="btn btn-sm revoke-filled revoke-all"
			disabled={revokingAll || otherSessions.length === 0}
			onclick={revokeAllOtherSessions}
		>
            <BadgeAlert size={16} />
			{revokingAll ? 'Завершаем...' : 'Завершить все другие сеансы'}
		</button>
		<p class="text-subtle revoke-all-caption">Выйти на всех устройствах, кроме текущего</p>
	{/if}

	{#if otherSessions.length > 0}
		<h3 class="h3 section-title">Активные сеансы</h3>
		<ul class="session-list">
			{#each otherSessions as session (session.sid)}
				{@const Icon = deviceIcons[session.device] ?? Globe}
				<li class="session-item">
					<div class="session-icon">
						<Icon size={20} />
					</div>
					<div class="session-info">
						<div class="session-title">{session.browser} · {session.os}</div>
						<div class="text-subtle">{session.device} · {session.country} · {session.ip}</div>
						<div class="text-subtle">Последняя активность: {formatDate(session.createdAt)}</div>
					</div>
					<button
						class="btn btn-secondary btn-sm revoke"
						disabled={revokingId === session.sid}
						onclick={() => revokeSession(session.sid)}
					>
						{revokingId === session.sid ? 'Завершаем...' : 'Завершить'}
					</button>
				</li>
			{/each}
		</ul>
	{/if}

    {#if successMessage}
        <div class="success-banner">{successMessage}</div>
    {/if}
{/if}

<style>
	.session-list {
		display: grid;
		gap: 0.6rem;
		list-style: none;
		margin: 1rem 0 0;
		padding: 0;
	}

	.session-item {
		display: flex;
		align-items: center;
		gap: 0.85rem;
		padding: 0.75rem;
		border: 1px solid var(--color-border);
		border-radius: var(--radius-md);
	}

    .section-title {
        margin: 1.25rem 0 0.6rem;
    }

    .section-title:first-of-type {
        margin-top: 1rem;
    }

    .session-item.current {
        border-color: var(--color-primary);
        background: var(--color-primary-soft);
    }

    .revoke-filled {
        background: var(--color-danger);
        color: var(--color-text-inverse);
        border-color: transparent;
    }

    .revoke-filled:hover {
        background: var(--color-accent);
    }

    .revoke-filled:disabled {
        background: var(--color-danger-soft);
        color: var(--color-danger);
        border-color: transparent;
    }

    .revoke-all {
        margin-top: 0.85rem;
        width: 100%;
    }

    .revoke-all-caption {
        margin: 0.35rem 0 0;
        text-align: center;
    }

	.session-icon {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 40px;
		height: 40px;
		flex: none;
		border-radius: var(--radius-md);
		background: var(--color-surface-hover);
		color: var(--color-text-muted);
	}

	.session-info {
		flex: 1;
		display: grid;
		gap: 0.15rem;
	}

	.session-title {
		font-weight: 600;
		display: flex;
		align-items: center;
		gap: 0.5rem;
	}

    .revoke {
        border-color: var(--color-danger);
        color: var(--color-danger);
        background: transparent;
    }

    .revoke:hover {
        background: var(--color-danger-soft);
    }

    .revoke:disabled {
        opacity: 0.6;
        color: var(--color-danger);
        background: transparent;
    }

	.badge {
		font-size: 0.7rem;
		font-weight: 700;
		padding: 0.1rem 0.5rem;
		border-radius: 999px;
		background: var(--color-success-soft);
		color: var(--color-success);
	}

    .success-banner {
        padding: 0.6rem 0.85rem;
        margin: 0.6rem 0;
        text-align: center;
        border-radius: var(--radius-md);
        background: var(--color-success-soft);
        color: var(--color-success);
        font-size: 0.875rem;
        font-weight: 600;
    }

	.error {
		color: var(--color-danger);
		font-size: 0.9rem;
	}
</style>
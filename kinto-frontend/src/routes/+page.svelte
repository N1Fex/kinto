<script lang="ts">
	import { accessToken } from "$lib/stores/tokenStore";
	import Avatar from "$lib/components/ui/Avatar.svelte";
    import ThemeButton from "$lib/components/ThemeButton.svelte";
    import AuthCard from "$lib/components/auth/AuthCard.svelte";

	let authMode = $state<"login" | "register">("login");

	const posts = [
		{
			author: "Маша Воронцова",
			handle: "masha",
			time: "38 мин",
			text: "Собираю маленькую подборку независимых книжных. Где в городе вам нравится читать и работать?",
			reactions: 18,
			comments: 6,
		},
		{
			author: "Илья Петров",
			handle: "ilya",
			time: "2 ч",
			text: "Наконец-то перестал добавлять функции «на всякий случай». Интерфейс сразу стал спокойнее — и в коде, и на экране.",
			reactions: 32,
			comments: 9,
		},
		{
			author: "Соня Ким",
			handle: "sonya",
			time: "вчера",
			text: "Ищу людей для еженедельных прогулок без цели: час по району, кофе и разговоры о том, что сейчас интересно.",
			reactions: 11,
			comments: 3,
		},
	];
		
</script>

<svelte:head

	><title>{authMode === "login" ? "Вход" : "Регистрация"} | Kinto</title><meta
		name="description"
		content="Войдите в Kinto или создайте аккаунт"
	/></svelte:head
>

{#if !$accessToken}
	<ThemeButton style="position: absolute; top: 24px; right:24px; border: 1px solid var(--color-border); background: var(--color-surface)"/>
	<section class="auth-page">
		<div class="auth-intro">
			<p class="eyebrow">Kinto</p>
			<h1 class="h1">Разговоры, к которым хочется вернуться.</h1>
			<p class="text-body text-muted">
				Войдите, чтобы читать ленту, писать посты и общаться с людьми.
			</p>
		</div>
		<AuthCard bind:mode={authMode}/>
	</section>
{:else}
	<section class="feed-head">
		<div>
			<p class="eyebrow">Для вас</p>
			<h1 class="h2">Что происходит сегодня</h1>
		</div>
		<button class="btn btn-ghost btn-sm">Свежее</button>
	</section>
	<section class="composer card">
		<!-- <Avatar src={$user.avatarUrl} name={$user.name} size={36} /><input
			class="input"
			placeholder="Поделитесь мыслью"
			aria-label="Новый пост"
		/><button class="btn btn-primary btn-sm">Опубликовать</button> -->
	</section>
	<div class="feed">
		{#each posts as post}<article class="post">
				<div class="post-top">
					<Avatar name={post.author} size={38} />
					<div>
						<p class="h3">{post.author}</p>
						<p class="text-subtle">@{post.handle} · {post.time}</p>
					</div>
				</div>
				<p class="post-text">{post.text}</p>
				<div class="post-actions">
					<button>Нравится <span>{post.reactions}</span></button
					><button>Комментарии <span>{post.comments}</span></button
					><button>Сохранить</button>
				</div>
			</article>{/each}
	</div>
{/if}

<style>
	.auth-page {
		display: grid;
		grid-template-columns: minmax(0, 1fr) minmax(320px, 390px);
		align-items: center;
		gap: clamp(2rem, 8vw, 6rem);
		min-height: 620px;
	}
	.auth-intro .h1 {
		max-width: 480px;
		font-size: clamp(2rem, 5vw, 3.1rem);
	}
	.auth-intro .text-body {
		max-width: 430px;
		margin-top: 1rem;
	}
	.eyebrow {
		margin: 0 0 0.45rem;
		color: var(--color-primary);
		font-size: 0.78rem;
		font-weight: 750;
		letter-spacing: 0.08em;
		text-transform: uppercase;
	}

	.feed-head {
		display: flex;
		align-items: end;
		justify-content: space-between;
		padding-bottom: 0.8rem;
		border-bottom: 1px solid var(--color-border);
	}
	.feed-head .eyebrow {
		margin-bottom: 0.25rem;
	}
	.composer {
		display: grid;
		grid-template-columns: auto minmax(0, 1fr) auto;
		gap: 0.65rem;
		align-items: center;
		margin: 1rem 0;
		padding: 0.75rem;
	}
	.feed {
		display: grid;
	}
	.post {
		padding: 1.15rem 0 1rem;
		border-bottom: 1px solid var(--color-border);
	}
	.post-top {
		display: flex;
		align-items: center;
		gap: 0.65rem;
	}
	.post-text {
		margin: 0.8rem 0;
		font-size: 0.96rem;
		line-height: 1.6;
	}
	.post-actions {
		display: flex;
		flex-wrap: wrap;
		gap: 0.15rem;
	}
	.post-actions button {
		padding: 0.3rem 0.45rem;
		border: 0;
		border-radius: 6px;
		background: transparent;
		color: var(--color-text-muted);
		font-size: 0.8125rem;
		cursor: pointer;
	}
	.post-actions button:hover {
		background: var(--color-surface-hover);
		color: var(--color-text);
	}
	.post-actions span {
		color: var(--color-text-subtle);
	}
	@media (max-width: 720px) {
		.auth-page {
			grid-template-columns: 1fr;
			min-height: auto;
			padding: 2.5rem 0;
		}
		.auth-intro .h1 {
			font-size: 2.2rem;
		}
		.composer {
			grid-template-columns: auto 1fr;
		}
		.composer .btn {
			grid-column: 2;
			justify-self: end;
		}
	}
</style>

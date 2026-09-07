<script lang="ts">
	import Avatar from '$lib/components/ui/Avatar.svelte';

	const chats = [
		{ id: 'masha', name: 'Маша Воронцова', preview: 'Давай в субботу после обеда', time: '12:42', unread: 2 },
		{ id: 'club', name: 'Книжный клуб', preview: 'Соня: принесу обе книги', time: 'вчера', unread: 0 },
		{ id: 'ilya', name: 'Илья Петров', preview: 'Спасибо, это помогло', time: 'вчера', unread: 0 }
	];
	let selectedId = $state('masha');
	let message = $state('');
	let sent = $state<string[]>([]);
	let selected = $derived(chats.find((chat) => chat.id === selectedId) ?? chats[0]);

	function send() {
		if (!message.trim()) return;
		sent = [...sent, message.trim()];
		message = '';
	}
</script>

<svelte:head><title>Сообщения — Kinto</title></svelte:head>

<section class="messenger">
	<div class:hidden-mobile={selectedId !== ''} class="chat-list">
		<div class="list-heading"><h1 class="h2">Сообщения</h1><button class="btn btn-ghost btn-sm">Новое</button></div>
		<input class="input" placeholder="Поиск диалогов" aria-label="Поиск диалогов" />
		<div class="dialogs">
			{#each chats as chat}
				<button class:chosen={chat.id === selectedId} onclick={() => selectedId = chat.id}>
					<Avatar name={chat.name} size={38} />
					<span class="dialog-copy"><b>{chat.name}</b><small>{chat.preview}</small></span>
					<span class="dialog-meta">{chat.time}{#if chat.unread}<i>{chat.unread}</i>{/if}</span>
				</button>
			{/each}
		</div>
	</div>

	<div class:visible-mobile={selectedId !== ''} class="conversation">
		<header class="conversation-head"><button class="back" onclick={() => selectedId = ''}>Назад</button><Avatar name={selected.name} size={32} /><div><b>{selected.name}</b><small>в сети недавно</small></div></header>
		<div class="messages"><div class="date">Сегодня</div><p class="bubble other">Привет! Ты всё ещё собираешь идеи для встречи?</p><p class="bubble mine">Да, как раз выбираю место. Хочется без шума.</p><p class="bubble other">Тогда давай в субботу после обеда.</p>{#each sent as item}<p class="bubble mine">{item}</p>{/each}</div>
		<form class="composer" onsubmit={(event) => { event.preventDefault(); send(); }}><input class="input" bind:value={message} placeholder="Напишите сообщение" /><button class="btn btn-primary" type="submit">Отправить</button></form>
	</div>
</section>

<style>
	.messenger { width: 100%; border: 0; border-radius: 0; box-shadow: none; display: grid; grid-template-columns: minmax(260px, 320px) minmax(0, 1fr); min-height: 100vh; background: var(--color-surface); overflow: hidden; }
	.chat-list { border-right: 1px solid var(--color-border); padding: .9rem; }.list-heading { display: flex; align-items: center; justify-content: space-between; margin-bottom: .8rem; }.dialogs { margin-top: .75rem; }.dialogs button { display: flex; width: 100%; align-items: center; gap: .65rem; border: 0; border-radius: var(--radius-md); background: transparent; padding: .7rem .5rem; text-align: left; cursor: pointer; }.dialogs button:hover, .dialogs button.chosen { background: var(--color-surface-hover); }.dialog-copy { display: grid; min-width: 0; gap: .16rem; flex: 1; }.dialog-copy b, .dialog-copy small { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }.dialog-copy small, .dialog-meta { color: var(--color-text-subtle); font-size: .75rem; }.dialog-meta { display: grid; gap: .35rem; justify-items: end; }.dialog-meta i { display: grid; width: 1.2rem; height: 1.2rem; place-items: center; border-radius: 50%; background: var(--color-primary); color: var(--color-text-inverse); font-size: .68rem; font-style: normal; }
	.conversation { display: grid; grid-template-rows: auto 1fr auto; min-width: 0; }.conversation-head { display: flex; align-items: center; gap: .55rem; border-bottom: 1px solid var(--color-border); padding: .8rem 1rem; }.conversation-head div { display: grid; gap: .1rem; }.conversation-head small { color: var(--color-text-subtle); font-size: .75rem; }.back { display: none; border: 0; background: transparent; color: var(--color-text-muted); cursor: pointer; }.messages { display: flex; flex-direction: column; gap: .55rem; padding: 1.2rem; }.date { align-self: center; color: var(--color-text-subtle); font-size: .75rem; }.bubble { max-width: 78%; margin: 0; border-radius: 12px; padding: .6rem .75rem; font-size: .9rem; line-height: 1.4; }.other { align-self: start; background: var(--color-surface-hover); }.mine { align-self: end; background: var(--color-primary); color: var(--color-text-inverse); }.composer { display: flex; gap: .55rem; border-top: 1px solid var(--color-border); padding: .75rem; }
	@media (max-width: 620px) { .messenger { display: block; min-height: 520px; }.conversation { display: none; min-height: 520px; }.conversation.visible-mobile { display: grid; }.chat-list.hidden-mobile { display: none; }.back { display: block; } }
</style>
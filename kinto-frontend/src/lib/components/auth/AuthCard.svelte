<script lang="ts">
    import LoginCard from "./LoginCard.svelte";
    import RegisterCard from "./RegisterCard.svelte";

	type Mode = "login" | "register" | "verification"

    let { mode = $bindable() }  = $props<{mode: Mode}>();
	let isMailVerification = $state(false);


	function switchMode(nextMode: Mode) {
		mode = nextMode;
	}

</script>

<div class="auth-card card" class:hidden={isMailVerification}>

    <div class="auth-tabs">
        <button
            class:active={mode === "login"}
            onclick={() => switchMode("login")}>Войти</button
        ><button
            class:active={mode === "register"}
            onclick={() => switchMode("register")}
            >Создать аккаунт</button
        >
    </div>

	{#if mode === "register"}
		<RegisterCard />
	{:else}
		<LoginCard />
	{/if}

    {#if mode === "login"}
        <p class="demo-hint">
            Для демо можно ввести любые непустые данные.
        </p>
    {/if}

</div>

<style>
    .auth-card {
		padding: 1.1rem;
	}
	.auth-tabs {
		display: grid;
		grid-template-columns: 1fr 1fr;
		gap: 0.2rem;
		border-bottom: 1px solid var(--color-border);
	}
	.auth-tabs button {
		border: 0;
		border-bottom: 2px solid transparent;
		background: transparent;
		padding: 0.7rem 0.3rem;
		color: var(--color-text-muted);
		font-weight: 650;
		cursor: pointer;
	}
	.auth-tabs button.active {
		border-color: var(--color-primary);
		color: var(--color-primary);
	}

	.demo-hint {
		margin: 0.9rem 0 0;
		color: var(--color-text-subtle);
		font-size: 0.75rem;
		text-align: center;
	}

	.hidden {
		display: none;
	}

</style>
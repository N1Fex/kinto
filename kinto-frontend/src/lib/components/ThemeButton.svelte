<script lang="ts">
    import { setTheme, theme } from '$lib/theme';
    import { Sun, Moon } from '@lucide/svelte';

    function toggleTheme() { 
        setTheme($theme === 'light' ? 'dark' : 'light');
    }

    let { 
        children = undefined, 
        className = "",
        style = "",
    } = $props();

</script>

<button 
    onclick={(event) => {toggleTheme(); event.currentTarget.blur(); }} 
    class={className}
    style={style}
    >

    {#if $theme == 'dark'}
        <Moon class="icon" size={24} strokeWidth={1.8} aria-hidden="true" />
    {:else}
        <Sun class="icon" size={24} strokeWidth={1.8} aria-hidden="true" />
    {/if}

    {@render children?.()}
</button>

<style>
    button:hover {
		background: var(--color-primary-soft);
		color: var(--color-primary);
	}

    button {
		display: flex;
		align-items: center;
		border-radius: var(--radius-md);
		color: var(--color-text-muted);
		font-weight: 600;
		padding: 0.8rem;
	}
</style>
<script lang="ts">
	import { roleInfo, type Role } from '$lib/roles';

	let { role = 'USER' } = $props<{ role?: Role }>();
	let info = $derived(roleInfo(role));
</script>

{#if role !== 'USER'}
	<span class="badge {info.badgeClass}">{info.label}</span>
{/if}
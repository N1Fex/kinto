<script lang="ts">
    import { apiFetch } from "$lib/httpClient";
    import { setUser } from "$lib/stores/userStore";
	import { setAccessToken } from "$lib/stores/tokenStore";

    let CODE_LENGTH = 6;
    let { 
        email = "",
        className = "",
        hidden = false,
    } = $props();
    
    let verificationCode = $state("");
    let checking = $state(false);
    let inputEl = $state();
    let error = $state<string | null>(null);

    function focusInput() {
        inputEl?.focus();
    }

    function submit(event: SubmitEvent) {
		event.preventDefault();
		error = "";
		
        verifyCode();
	}

    function handleInput(event: any) {
        const newValue = event.target.value.replace(/\D/g, '');
        verificationCode = newValue.slice(0, 6);
        
        verifyCode();
    }

    function verifyCode() {
        error = null;
        if (verificationCode.length !== 6) {
            return;
        }
        checking = true;
        let res = apiFetch("/auth/verify-email", {
            method: "POST",
            body: JSON.stringify({
                "email": email,
                "verificationCode": verificationCode
            })
        });
        res.then(async (response) => {
            if (!response.ok) {
                const errorBody = await response.json().catch(() => null);
                throw new Error(errorBody?.message || `HTTP ${response.status}`);
            }
            error = null
            
            let data = await response.json();
            const { accessToken, user } = data;
			setAccessToken(accessToken);
            setUser(user);
        }).catch(err => {
            error = err?.message;
        }).finally(() => {
            checking = false
        })
    }

     let cells = $derived(
        Array.from({ length: CODE_LENGTH }, (_, i) => verificationCode[i] ?? '')
    );

</script>


<div class="auth-card card {className}" hidden={hidden}>
    <form onsubmit={submit}>
        
        <span>Подтверждение почты <b>{email}</b></span>

        <div class="code-wrapper" class:disabled={checking} onclick={focusInput}>
            
            <input
                bind:this={inputEl}
                class="code-input-hidden"
                type="text"
                inputmode="numeric"
                autocomplete="one-time-code"
                maxlength={CODE_LENGTH}
                bind:value={verificationCode}
                disabled={checking}
                oninput={handleInput}
            />

            <div class="code-cells" aria-hidden="true">
                {#each cells as ch, i}
                    <div
                        class="cell"
                        class:filled={ch !== ''}
                        class:active={i === verificationCode.length}
                    >
                        {ch || '_'}
                    </div>
                {/each}
            </div>
            
        </div>
        {#if error}<p class="error">{error}</p>{/if}
        <button 
            class="btn btn-primary submit"
            type="submit"
            disabled={checking || verificationCode.length !== 6}
            >
            Подтвердить
        </button>
    </form>
</div>

<style>
    .auth-card {
		padding: 1.1rem;
	}

    .code-wrapper {
        position: relative;
        width: 100%;
    }

    .code-input-hidden {
        position: absolute;
        inset: 0;
        width: 100%;
        height: 100%;
        opacity: 0;
        border: none;
        padding: 0;
        margin: 0;
        cursor: text;
        z-index: 2;
    }

    .code-cells {
        display: flex;
        gap: 10px;
        justify-content: center;
        pointer-events: none;
    }

    .cell {
        flex: 1;
        aspect-ratio: 1 / 1.15;
        max-width: 64px;

        display: flex;
        align-items: center;
        justify-content: center;

        font-size: 32px;
        font-weight: 600;
        color: var(--color-text);

        background: var(--color-surface);
        border: 2px solid var(--color-border);
        border-radius: var(--radius-md);
        box-shadow: var(--shadow-card);

        transition: border-color 0.15s ease, box-shadow 0.15s ease;
    }

    .cell:not(.filled) {
        color: var(--color-text-subtle);
    }

    .cell.filled {
        border-color: var(--color-border-strong);
    }

    .cell.active {
        border-color: var(--color-primary);
        box-shadow: 0 0 0 3px var(--color-primary-soft);
    }

    .code-wrapper.disabled {
        opacity: 0.6;
        pointer-events: none;
    }

    .verification {
        width: 100%;
        max-width: 320px;
        height: 80px;

        font-size: 40px;
        font-weight: 600;
        text-align: center;
        letter-spacing: 16px; /* расстояние между "ячейками" */

        border: 2px solid #d0d0d5;
        border-radius: 16px;
        background: #fff;
        color: #1a1a1a;

        outline: none;
        transition: border-color 0.2s ease, box-shadow 0.2s ease;
    }

    .verification::placeholder {
        color: #c4c4cc;
        letter-spacing: 16px;
    }

    .verification:focus {
        border-color: #6366f1;
        box-shadow: 0 0 0 4px rgba(99, 102, 241, 0.15);
    }

    .verification:disabled {
        opacity: 0.6;
        cursor: not-allowed;
    }

	.submit {
		width: 100%;
		margin-top: 0.25rem;
	}
	.error {
		margin: 0;
		color: var(--color-danger);
		font-size: 0.82rem;
	}

</style>
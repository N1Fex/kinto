<script lang="ts">
    import { apiFetch, rawApiFetch } from "$lib/httpClient";
    import { type RegisterRequestDto } from "$lib/types";
    import EmailVerificationCard from "./EmailVerificationCard.svelte";
    
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9\s])\S{8,}$/;
    const usernameRegex = /^[A-Za-z0-9_]{3,64}$/;

    function isValidPassword(password: string): boolean {
    return passwordRegex.test(password);
    }

    let dto: RegisterRequestDto = $state({
        email: "",
        username: "",
        displayName: "",
        password: "",
        confirmPassword: "",
    });
    
    let username = $derived(dto.username)

    let isUsernameAvailable = $state<boolean | null>(null)
    let isChecking = $state<boolean>(false);
    let isMailVerification = $state<boolean>(false);
    let error = $state<string | null>('');
    let usernameError = $state<string | null>('');
    
    let debounceTimer: number | undefined;
    let abortController: AbortController;

    async function checkUsername(value: string) {
        
        if (!usernameRegex.test(value)) {
            isUsernameAvailable = null;
            usernameError = "Минимум 3 символа: латинские буквы, цифры и _"
            return;
        }

        abortController?.abort();
        abortController = new AbortController();

        isChecking = true;
        isUsernameAvailable = null;
        usernameError = '';

        try {
            const res = await rawApiFetch(`/users/check-username?username=${encodeURIComponent(value)}`, {
                signal: abortController.signal
            });
        
            if (!res.ok) throw new Error('Ошибка сервера');
            
            const data = await res.json();
            isUsernameAvailable = data.available;
        } catch (err: any) {
            if (err.name === 'AbortError') return; // игнорируем отменённые запросы
            usernameError = 'Не удалось проверить';
            isUsernameAvailable = null;
        } finally {
            isChecking = false;
        }
    }

    function handleInput() {
        clearTimeout(debounceTimer);
        
        isUsernameAvailable = null;
        usernameError = '';

        if (!username.trim()) {
            isChecking = false;
            return;
        }

        debounceTimer = setTimeout(() => {
            checkUsername(username);
        }, 500);
    }

    function handleBlur() {
        clearTimeout(debounceTimer);
        if (username.trim()) {
            checkUsername(username);
        }
    }

    function onSubmit() {
        usernameError = null
        error = null
        // Проверить поля
        if (dto.password != dto.confirmPassword) {
            error = "Пароли не совпадают"
            return;
        } 

        // Кинуть запрос
        let res = apiFetch("/auth/register", {
            method: "POST",
            body: JSON.stringify(dto)
        });

        res.then(async (response) => {
            if (!response.ok) {
                const errorBody = await response.json().catch(() => null);
                throw new Error(errorBody?.message || `HTTP ${response.status}`);
            }
            isMailVerification = true
        }).catch(err => {
            error = err?.message
        })
    }
</script>

{#if !isMailVerification}
    <form onsubmit={onSubmit} >

        <label>
            <span class="label-text">Имя</span>
            <input
                class="input"
                placeholder="Tom"
                bind:value={dto.displayName}
                aria-placeholder="dd"
                autocomplete="name"
                required
            /></label
        >
        <label
            >Имя пользователя
            <div class="username-container">
                <span>@</span>
                <input
                    placeholder="user123"
                    class="input"
                    bind:value={dto.username}
                    oninput={handleInput}
                    onblur={handleBlur}
                    autocomplete="username"
                />
            </div>
            {#if isChecking}
                <span class="status">Проверяем...</span>
            {:else if isUsernameAvailable === true}
                <span class="status available">✓ Свободно</span>
            {:else if isUsernameAvailable === false}
                <span class="status taken">✗ Занято</span>
            {:else if usernameError}
                <span class="status error">{usernameError}</span>
            {/if}
        </label>

        

        <label>
            <span class="label-text">Email</span>
            <input
                class="input"
                type="email"
                bind:value={dto.email}
                autocomplete="email"
                required
            />
        </label>
        <label>
            <span class="label-text">Пароль</span>
            <input
                class="input"
                type="password"
                bind:value={dto.password}
                autocomplete="new-password"
                required
            />
        </label>
        {#if dto.password && !isValidPassword(dto.password)}
            <span class="error">
                Пароль должен содержать минимум 8 символов, цифру, строчную и заглавную букву и спецсимвол.
            </span>
        {/if}
        <label>
            <span class="label-text">Повторить пароль</span>
            <input
                class="input"
                type="password"
                bind:value={dto.confirmPassword}
                autocomplete="new-password"
                required
            />
        </label>
        {#if error}
            <span class="error">
                {error}
            </span>
        {/if}
        <button class="btn btn-primary submit" type="submit"
            >Создать аккаунт</button>
    </form>
{:else}
    <EmailVerificationCard email={dto.email} hidden={!isMailVerification}/>
{/if}


<style>
    label {
		display: grid;
		gap: 0.35rem;
		color: var(--color-text-muted);
		font-size: 0.8rem;
		font-weight: 650;
	}

    .submit {
		width: 100%;
		margin-top: 0.25rem;
	}

    .username-container {
        display: flex;
        flex-direction: row;
    }

    .username-container span {
        font-size: large;
        font-weight: bold;
        text-align: center;

        border: 1px solid var(--color-border-strong);
        border-radius: var(--radius-md);
        border-top-right-radius: 0;
        border-bottom-right-radius: 0;
        background: var(--color-surface-raised);
        color: var(--color-text);
        padding: 0.4rem 0.7rem;
    }

    .username-container input {
        border-top-left-radius: 0;
        border-bottom-left-radius: 0;
    }

    .status { 
        font-size: 0.82rem; 
        margin-top: 0.25rem; 
        display: block; 
    }
    
    .available { 
        color: var(--color-success); 
    }
    
    .taken { 
        color: var(--color-danger); 
    }

	.error {
		margin: 0;
		color: var(--color-danger);
		font-size: 0.82rem;
	}

    label:has(input[required]) > .label-text::after {
        content: "*";
        color: red;
        margin-left: 0.4rem;
    }
</style>
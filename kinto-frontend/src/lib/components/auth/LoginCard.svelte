<script lang="ts">
    import { setUser } from "$lib/stores/userStore";
    import { rawApiFetch } from "$lib/httpClient"
	import { setAccessToken } from "$lib/stores/tokenStore";
    import { type LoginRequestDto } from "$lib/types";

    let error = $state<string | null>(null);

    function handleSubmit() {
        error = null

        if (dto.login.length < 3) {
            error = "Слишком короткий логин"
            return;
        }
        if (dto.password == "") {
            error = "Введите пароль"
            return;
        };

        const res = rawApiFetch('/auth/login', {
			method: 'POST',
			credentials: 'include',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(dto),
		});

        res.then(async (response) => {
            if (!response.ok) {
                const errorBody = await response.json().catch(() => null);
                throw new Error(errorBody?.message || `HTTP ${response.status}`);
            }
            return response.json();
            
        }).then((data) => {
            const { accessToken, user } = data;
			setAccessToken(accessToken);
            setUser(user);
        }).catch((err) => {
            error = err.message;
        })
    }

    let dto: LoginRequestDto = $state({
        login: "",
        password: "",
    });
</script>

<form onsubmit={handleSubmit}>
    <label>
        Имя пользователя или email
        <input
            class="input"
            bind:value={dto.login}
            autocomplete="username"
        />
    </label>
    <label
        >Пароль<input
            class="input"
            type="password"
            bind:value={dto.password}
            autocomplete="current-password"
        />
    </label>

    <span class:error hidden={!error}>
        {error}
    </span>

    <button class="btn btn-primary submit" type="submit"
        >Войти</button>
</form>

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

	.error {
		margin: 0;
		color: var(--color-danger);
		font-size: 0.82rem;
	}
</style>
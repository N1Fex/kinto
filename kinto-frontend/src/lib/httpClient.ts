import { getAccessToken, setAccessToken } from './stores/tokenStore';
import { setUser } from "$lib/stores/userStore";

const BASE_URL = 'http://192.168.0.2:8080/api/v1';

let refreshPromise: Promise<string | null> | null = null;

async function refreshAccessToken(): Promise<string | null> {
  if (refreshPromise) return refreshPromise;

  refreshPromise = fetch(`${BASE_URL}/auth/refresh`, {
    method: 'POST',
    credentials: 'include', 
  })
    .then(async (res) => {
      if (!res.ok) return null;
      const data = await res.json();
      setUser(data.user);
      return data.accessToken as string;
    })
    .catch(() => null)
    .finally(() => {
      refreshPromise = null;
    });

  return refreshPromise;
}

export async function initialRefreshTokens() {
    const newToken = await refreshAccessToken();
    if (newToken) {
      setAccessToken(newToken);
    } else {
      setAccessToken(null);
    }
}

export async function rawApiFetch(path: string, options: RequestInit = {}): Promise<Response> {

    const doRequest = (token: string | null) =>
        fetch(`${BASE_URL}${path[0]=='/' ? "" : "/"}${path}`, {
        ...options,
        headers: {
            'Content-Type': 'application/json',
            ...(token ? { Authorization: `Bearer ${token}` } : {}),
            ...options.headers,
        },
        });

    return await doRequest(getAccessToken());
}

export async function apiFetch(path: string, options: RequestInit = {}): Promise<Response> {
  const doRequest = (token: string | null) =>
    fetch(`${BASE_URL}${path[0]=='/' ? "" : "/"}${path}`, {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
        ...options.headers,
      },
    });

  let response = await doRequest(getAccessToken());

  if (response.status === 401) {
    const newToken = await refreshAccessToken();

    if (newToken) {
      setAccessToken(newToken);
      response = await doRequest(newToken);
    } else {
      setAccessToken(null);
      window.location.href = '/';
    }
  }

  return response;
}
import { writable } from 'svelte/store';

export const accessToken = writable<string | null>(null);

let currentToken: string | null = null;
accessToken.subscribe((value) => {
  currentToken = value;
});

export function getAccessToken(): string | null {
  return currentToken;
}

export function setAccessToken(token: string | null) {
  accessToken.set(token);
}
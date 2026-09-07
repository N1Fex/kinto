import { browser } from '$app/environment';
import { writable } from 'svelte/store';
import { DEFAULT_THEME, isTheme, type Theme } from './themes';

const STORAGE_KEY = 'kinto-theme';
const TRANSITION_DURATION = 220;
let transitionTimer: ReturnType<typeof setTimeout> | undefined;

export const theme = writable<Theme>(DEFAULT_THEME);

function applyTheme(nextTheme: Theme) {
	if (browser) document.documentElement.dataset.theme = nextTheme;
}

export function setTheme(nextTheme: Theme) {
	if (browser) {
		document.documentElement.classList.add('theme-transition');
		if (transitionTimer) clearTimeout(transitionTimer);
		transitionTimer = setTimeout(() => {
			document.documentElement.classList.remove('theme-transition');
			transitionTimer = undefined;
		}, TRANSITION_DURATION);
	}

	applyTheme(nextTheme);
	theme.set(nextTheme);
	if (browser) localStorage.setItem(STORAGE_KEY, nextTheme);
}

export function initializeTheme() {
	if (!browser) return;
	const savedTheme = localStorage.getItem(STORAGE_KEY);
	const nextTheme = isTheme(savedTheme) ? savedTheme : DEFAULT_THEME;
	applyTheme(nextTheme);
	theme.set(nextTheme);
}

export type { Theme } from './themes';
export { DEFAULT_THEME, themes } from './themes';
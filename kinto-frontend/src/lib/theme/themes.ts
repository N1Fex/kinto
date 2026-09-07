export const themes = ['light', 'dark'] as const;
export type Theme = (typeof themes)[number];
export const DEFAULT_THEME: Theme = 'light';

export function isTheme(value: string | null): value is Theme {
	return value !== null && (themes as readonly string[]).includes(value);
}
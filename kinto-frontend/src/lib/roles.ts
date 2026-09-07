export type Role = 'USER' | 'MODERATOR' | 'ADMIN' | 'SUPERADMIN';

interface RoleInfo {
	label: string;
	badgeClass: string;
	rank: number;
}

/**
 * Единая точка правды по ролям: подпись, класс бейджа, права на модерацию.
 * USER < MODERATOR < ADMIN < SUPERADMIN
 */
export const ROLES: Record<Role, RoleInfo> = {
	USER: { label: 'Пользователь', badgeClass: 'badge-user', rank: 0 },
	MODERATOR: { label: 'Модератор', badgeClass: 'badge-moderator', rank: 1 },
	ADMIN: { label: 'Администратор', badgeClass: 'badge-admin', rank: 2 },
	SUPERADMIN: { label: 'Суперадмин', badgeClass: 'badge-superadmin', rank: 3 }
};

export function roleInfo(role: Role): RoleInfo {
	return ROLES[role] ?? ROLES.USER;
}

export function hasMinRole(role: Role, minRole: Role): boolean {
	return roleInfo(role).rank >= roleInfo(minRole).rank;
}
 
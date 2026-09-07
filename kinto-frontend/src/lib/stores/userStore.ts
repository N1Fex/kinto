import { writable, type Writable } from 'svelte/store';
import type { Role } from '$lib/roles';

export interface User {
	id: string;
	username: string;
	name: string;
	email: string;
	avatarUrl: string | null;
	role: Role;
	bio: string;
}

export const user: Writable<User | null> = writable(null);

export function setUser(nextUser: User): void { 
	user.set(nextUser); 
}

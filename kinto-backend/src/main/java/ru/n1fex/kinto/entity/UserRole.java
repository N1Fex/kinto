package ru.n1fex.kinto.entity;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    MODERATOR("ROLE_MODERATOR"),
    SUPERADMIN("ROLE_SUPERADMIN");

    private final String authority;
    UserRole(String authority) {
        this.authority = authority;
    }

    @Override
    public @Nullable String getAuthority() {
        return this.authority;
    }

    @Override
    public String toString() {
        return this.name();
    }
}

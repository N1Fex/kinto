package ru.n1fex.kinto.util;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.n1fex.kinto.dto.JwtAuthentication;
import ru.n1fex.kinto.entity.UserRole;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generateAuthentication(Claims claims) {
        final JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setRole(UserRole.valueOf(claims.get("role", String.class)));
        jwtAuthentication.setSid(UUID.fromString(claims.get("sid", String.class)));
        jwtAuthentication.setUserId(Long.valueOf(claims.getSubject()));
        return jwtAuthentication;
    }
}

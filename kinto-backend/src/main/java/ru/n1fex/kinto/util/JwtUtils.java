package ru.n1fex.kinto.util;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.n1fex.kinto.dto.JwtAuthentication;
import ru.n1fex.kinto.entity.UserRole;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setRole(claims.get("role", UserRole.class));
        jwtAuthentication.setName(claims.get("name", String.class));
        jwtAuthentication.setEmail(claims.getSubject());
        return jwtAuthentication;
    }
}

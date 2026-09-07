package ru.n1fex.kinto.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import ru.n1fex.kinto.util.JwtProvider;

@Component
public class CookieService {
    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    private final JwtProvider jwtProvider;
    private final String baseUrl;

    public CookieService(
            JwtProvider jwtProvider,
            @Value("${server.servlet.context-path}") String baseUrl) {
        this.jwtProvider = jwtProvider;
        this.baseUrl = baseUrl;
    }

    public ResponseCookie createRefreshTokenCookie(String token) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE, token)
                .httpOnly(true)
                //.secure(true)
                .sameSite("Strict")
                .path(baseUrl+"auth")          // ограничиваем путь — refresh token уходит только на /auth/**
                .maxAge(jwtProvider.getRefreshTtl())
                .build();
    }

    public ResponseCookie clearCookie(String name, String path) {
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                //.secure(true)
                .sameSite("Strict")
                .path(baseUrl+path)
                .maxAge(0)
                .build();
    }
}

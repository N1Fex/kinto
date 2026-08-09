package ru.n1fex.kinto.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.n1fex.kinto.entity.User;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtProvider {

    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;
    @Getter
    private final Duration refreshTtl;

    public JwtProvider(
            @Value("${jwt.secret.access}") String jwtAccessSecret,
            @Value("${jwt.secret.refresh}") String jwtRefreshSecret,
            @Value("${jwt.refresh.ttl}") Duration ttl) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
        this.refreshTtl = ttl;
    }

    public String generateAccessToken(@NonNull User user, @NonNull UUID jti) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExp = Date.from(accessExpInstant);
        return Jwts.builder()
                .subject(user.getId().toString())
                .expiration(accessExp)
                .signWith(jwtAccessSecret)
                .claim("role", user.getRole().name())
                .claim("status", user.getStatus())
                .claim("sid", jti.toString())
                .compact();
    }

    public String generateRefreshToken(@NonNull User user, @NonNull UUID jti) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpInstant = now.plusDays(refreshTtl.toDays()).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExp = Date.from(refreshExpInstant);
        return Jwts.builder()
                .subject(user.getEmail())
                .expiration(refreshExp)
                .claim("jti", jti.toString())
                .signWith(jwtRefreshSecret)
                .compact();
    }

    public boolean validateAccessToken(@NonNull String token) {
        return this.validateToken(token, jwtAccessSecret);
    }

    public boolean validateRefreshToken(@NonNull String token) {
        return this.validateToken(token, jwtRefreshSecret);
    }

    private boolean validateToken(@NonNull String token, @NonNull SecretKey secret) {
        try {
            Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch(ExpiredJwtException expEx) {
            log.error("Expired token");
        } catch(UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt");
        } catch(MalformedJwtException malEx) {
            log.error("Malformed jwt");
        } catch(SignatureException sigEx) {
            log.error("Invalid signature");
        } catch(Exception ex) {
            log.error("Invalid token");
        }
        return false;
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessSecret);
    }
    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    private Claims getClaims(@NonNull String token, @NonNull SecretKey secret) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

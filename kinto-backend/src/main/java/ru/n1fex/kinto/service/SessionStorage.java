package ru.n1fex.kinto.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import ru.n1fex.kinto.dto.SessionDto;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SessionStorage {

    private final StringRedisTemplate redisTemplate;

    private SessionDto hashmapToSessionDto(Map<Object, Object> map) {
        return new SessionDto(
                Long.valueOf(map.get("userId").toString()),
                UUID.fromString(map.get("sid").toString()),
                (String) map.get("device"),
                (String) map.get("country"),
                (String) map.get("browser"),
                (String) map.get("os"),
                (String) map.get("ip"),
                Instant.parse((String) map.get("createdAt")),
                Instant.parse((String) map.get("expiresAt"))
        );
    }

    public SessionDto getSession(UUID sessionId) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries("refresh:" + sessionId.toString());

        if (map.isEmpty()) {
            return null;
        }
        return hashmapToSessionDto(map);
    }

    public List<SessionDto> getSessions(Long userId) {
        Set<String> jtis = redisTemplate.opsForSet().members("user:sessions:" + userId);

        return jtis.stream().map((jti) -> {
                Map<Object, Object> map = redisTemplate.opsForHash().entries("refresh:" + jti);

                // Если refresh истек - то не показывать эту сессию и удалить в sessions
                if (map.isEmpty()) {
                    redisTemplate.opsForHash().delete("user:sessions:" + userId, jti);
                    return null;
                }
                return hashmapToSessionDto(map);
            }).filter(Objects::nonNull).toList();
    }

    private void saveSession(SessionDto session, UUID sessionId, Duration ttl) {
        String key = "refresh:" + sessionId;

        redisTemplate.opsForHash().put(key, "userId", session.getUserId().toString());
        redisTemplate.opsForHash().put(key, "sid", session.getSid().toString());
        redisTemplate.opsForHash().put(key, "country", session.getCountry());
        redisTemplate.opsForHash().put(key, "ip", session.getIp());
        redisTemplate.opsForHash().put(key, "device", session.getDevice());
        redisTemplate.opsForHash().put(key, "browser", session.getBrowser());
        redisTemplate.opsForHash().put(key, "os", session.getOs());
        redisTemplate.opsForHash().put(key, "createdAt", Instant.now().toString());
        redisTemplate.opsForHash().put(key, "expiresAt", Instant.now().plus(ttl).toString());

        redisTemplate.expire(key, ttl);

    }

    public void saveRefreshToken(SessionDto session, UUID jti, Duration ttl) {
        saveSession(session, jti, ttl);
        // Добавляем jti в список сессий пользователя
        redisTemplate.opsForSet().add("user:sessions:" + session.getUserId(), jti.toString());
    }

    public boolean refreshExists(UUID jti) {
        return redisTemplate.hasKey("refresh:" + jti);
    }

    public void removeRefresh(Long userId, UUID jti) {
        redisTemplate.delete("refresh:" + jti);
        redisTemplate.opsForSet().remove("user:sessions:" + userId.toString(), jti.toString());
    }

    public void removeAllRefreshesExclude(Long userId, UUID jti) {
        Set<String> tokens = redisTemplate.opsForSet().members("user:sessions:" + userId.toString());
        tokens.remove(jti.toString()); // оставляем все, кроме переданного

        redisTemplate.opsForSet().remove("user:sessions:" + userId, tokens.toArray()); // удаляем все "сессии" кроме переданной
        redisTemplate.delete(tokens.stream().map(token -> "refresh:"+token).toList());
    }

    public void removeAllRefreshes(Long userId) {
        Set<String> tokens = redisTemplate.opsForSet().members("user:sessions:" + userId.toString());

        redisTemplate.delete("user:sessions:" + userId); // удаляем все "сессии"
        redisTemplate.delete(tokens.stream().map(token -> "refresh:"+token).toList());
    }

}

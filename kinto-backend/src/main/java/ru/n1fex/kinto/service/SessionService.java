package ru.n1fex.kinto.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.n1fex.kinto.dto.SessionDto;
import ru.n1fex.kinto.dto.SessionMetadata;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionStorage sessionStorage;

    public List<SessionDto> getSessions(Long userId) {
        return sessionStorage.getSessions(userId);
    }
    public SessionDto getSession(UUID sessionId) {
        return sessionStorage.getSession(sessionId);
    }


    public void logout(Long userId, UUID sessionId) {
        sessionStorage.removeRefresh(userId, sessionId);
    }

    public void logoutOthers(Long userId, UUID currentSessionId) {
        sessionStorage.removeAllRefreshesExclude(userId, currentSessionId);
    }

    public void logoutAll(Long userId) {
        sessionStorage.removeAllRefreshes(userId);
    }


    public SessionDto createNewSession(Long userId, UUID sid, Duration sessionTtl, SessionMetadata metadata) {
        SessionDto session = new SessionDto(
                userId,
                sid,
                metadata.device(),
                metadata.country(),
                metadata.browser(),
                metadata.os(),
                metadata.ip(),
                Instant.now(),
                Instant.now().plus(sessionTtl)
        );
        sessionStorage.saveRefreshToken(session, sid, sessionTtl);
        return session;
    }

    public Boolean refreshExists(UUID sessionId) {
        return sessionStorage.refreshExists(sessionId);
    }


    public void removeRefresh(Long userId, UUID sessionId) {
        sessionStorage.removeRefresh(userId, sessionId);
    }



}

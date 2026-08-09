package ru.n1fex.kinto.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.n1fex.kinto.dto.JwtAuthentication;
import ru.n1fex.kinto.dto.SessionDto;
import ru.n1fex.kinto.service.SessionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    @GetMapping
    public ResponseEntity<List<SessionDto>> getSessions(JwtAuthentication authentication) {
        return ResponseEntity.ok(
                sessionService.getSessions(authentication.getUserId())
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllSession(JwtAuthentication auth) {
        sessionService.logoutAll(auth.getUserId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/current")
    public ResponseEntity<Void> deleteCurrentSession(JwtAuthentication auth) {
        sessionService.logout(auth.getUserId(), auth.getSid());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/others")
    public ResponseEntity<Void> deleteOthersSession(JwtAuthentication auth) {
        sessionService.logoutOthers(auth.getUserId(), auth.getSid());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(JwtAuthentication auth, @PathVariable("sessionId") UUID sessionId) {
        sessionService.logout(auth.getUserId(), sessionId);
        return ResponseEntity.noContent().build();
    }


}

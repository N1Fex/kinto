package ru.n1fex.kinto.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.n1fex.kinto.dto.ActiveSessionsDto;
import ru.n1fex.kinto.dto.JwtAuthentication;
import ru.n1fex.kinto.service.CookieService;
import ru.n1fex.kinto.service.SessionService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;
    private final CookieService cookieService;

    @GetMapping
    public ResponseEntity<ActiveSessionsDto> getSessions(JwtAuthentication authentication) {

        return ResponseEntity.ok(
                new ActiveSessionsDto(
                        authentication.getSid().toString(),
                        sessionService.getSessions(authentication.getUserId())
                )
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
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookieService.clearCookie(CookieService.REFRESH_TOKEN_COOKIE, "/auth").toString())
                .build();
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

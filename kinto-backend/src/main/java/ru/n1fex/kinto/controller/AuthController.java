package ru.n1fex.kinto.controller;

import co.elastic.clients.util.Pair;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.n1fex.kinto.UserMapper;
import ru.n1fex.kinto.dto.*;
import ru.n1fex.kinto.entity.User;
import ru.n1fex.kinto.service.AuthService;
import ru.n1fex.kinto.service.CookieService;
import ru.n1fex.kinto.service.SessionMetadataService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final SessionMetadataService sessionMetadataService;

    private final UserMapper userMapper;
    private final CookieService cookieService;

    @PostMapping("/verify-email")
    public ResponseEntity<AuthResponse> verifyEmail(
            @RequestBody @Valid VerificationEmailRequest request,
            HttpServletRequest httpRequest
    ) {
        SessionMetadata metadata = sessionMetadataService.extract(httpRequest);
        Pair<JwtPair, User> pair =authService.verifyEmail(request.getEmail(), request.getVerificationCode(), metadata);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieService.createRefreshTokenCookie(pair.key().getRefreshToken()).toString())
                .body(new AuthResponse(
                                pair.key().getAccessToken(),
                                userMapper.toUserDto(pair.value())
                        )
                );
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegistrationRequest registrationRequest) {
        authService.register(registrationRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid JwtRequest jwtRequest,
            HttpServletRequest httpRequest
    ) {
        SessionMetadata metadata = sessionMetadataService.extract(httpRequest);
        Pair<JwtPair, User> pair = authService.login(jwtRequest.getLogin(), jwtRequest.getPassword(), metadata);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieService.createRefreshTokenCookie(pair.key().getRefreshToken()).toString())
                .body(new AuthResponse(
                        pair.key().getAccessToken(),
                        userMapper.toUserDto(pair.value())
                    )
                );
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue(name = CookieService.REFRESH_TOKEN_COOKIE, required = false) String refreshToken,
            HttpServletRequest httpRequest
    ) {
        SessionMetadata metadata = sessionMetadataService.extract(httpRequest);
        Pair<JwtPair, User> pair = authService.refreshTokens(refreshToken, metadata);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieService.createRefreshTokenCookie(pair.key().getRefreshToken()).toString())
                .body(new AuthResponse(
                                pair.key().getAccessToken(),
                                userMapper.toUserDto(pair.value())
                        )
                );
    }
}

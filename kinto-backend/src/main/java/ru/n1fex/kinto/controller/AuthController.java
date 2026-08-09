package ru.n1fex.kinto.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.n1fex.kinto.UserMapper;
import ru.n1fex.kinto.dto.*;
import ru.n1fex.kinto.service.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final UserMapper userMapper;

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody @Valid VerificationEmailRequest request) {
        authService.verifyEmail(request.getEmail(), request.getVerificationCode());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid RegistrationRequest registrationRequest) {
        return new ResponseEntity<>(
                userMapper.toUserDto(authService.register(registrationRequest)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid JwtRequest jwtRequest) {
        return ResponseEntity.ok(
                authService.login(jwtRequest.getLogin(), jwtRequest.getPassword())
        );
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody RefreshJwtRequest refreshRequest) {
        return authService.refreshTokens(refreshRequest);
    }
}

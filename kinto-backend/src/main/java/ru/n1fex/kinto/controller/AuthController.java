package ru.n1fex.kinto.controller;

import co.elastic.clients.elasticsearch.indices.RefreshRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.n1fex.kinto.UserMapper;
import ru.n1fex.kinto.dto.JwtRequest;
import ru.n1fex.kinto.dto.JwtResponse;
import ru.n1fex.kinto.dto.RegistrationRequest;
import ru.n1fex.kinto.dto.UserDto;
import ru.n1fex.kinto.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid RegistrationRequest registrationRequest) {
        // todo Заменить на JwtResponse
        return ResponseEntity.ok(
                userMapper.toUserDto(authService.register(registrationRequest))
        );
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid JwtRequest jwtRequest) {
        // todo Заменить на JwtResponse
        return ResponseEntity.ok(
                userMapper.toUserDto(authService.login(jwtRequest.getLogin(), jwtRequest.getPassword()))
        );
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody RefreshRequest refreshRequest) {

        return null;
    }
}

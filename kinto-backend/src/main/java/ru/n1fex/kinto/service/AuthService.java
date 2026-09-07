package ru.n1fex.kinto.service;

import co.elastic.clients.util.Pair;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.n1fex.kinto.dto.*;
import ru.n1fex.kinto.entity.User;
import ru.n1fex.kinto.entity.UserRole;
import ru.n1fex.kinto.entity.UserStatus;
import ru.n1fex.kinto.entity.VerificationChannel;
import ru.n1fex.kinto.exception.InvalidCredentialsException;
import ru.n1fex.kinto.exception.NotFoundException;
import ru.n1fex.kinto.exception.SomethingWentWrongException;
import ru.n1fex.kinto.exception.user.UserAlreadyExistsException;
import ru.n1fex.kinto.exception.user.UserBannedException;
import ru.n1fex.kinto.exception.user.UserNotFoundException;
import ru.n1fex.kinto.exception.user.UserUnverifiedException;
import ru.n1fex.kinto.repository.UserRepository;
import ru.n1fex.kinto.util.JwtProvider;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final VerificationService verificationService;
    private final SessionService sessionService;

    @Value("${verification.email.ttl}")
    private Duration verificationDuration;

    private JwtPair createNewSession(User user, SessionMetadata metadata) {
        UUID jti = UUID.randomUUID();
        String refreshToken = jwtProvider.generateRefreshToken(user, jti);
        String accessToken = jwtProvider.generateAccessToken(user, jti);
        sessionService.createNewSession(user.getId(), jti, jwtProvider.getRefreshTtl(), metadata);
        return new JwtPair(accessToken, refreshToken);

    }

    public Pair<JwtPair, User> login(String login, String password, SessionMetadata metadata) {
        Optional<User> userOpt = userRepository.getUserByUsernameOrEmail(login, login);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден!");
        }
        User user = userOpt.get();

        if (user.getStatus() == UserStatus.ACTIVE) {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new InvalidCredentialsException("Неверные данные для входа!");
            }

            return Pair.of(createNewSession(user, metadata), user);
        }

        if (user.getStatus() == UserStatus.UNVERIFIED) {
            throw new UserUnverifiedException("Почта не была подтверждена");
        }
        if (user.getStatus() == UserStatus.BANNED) {
            throw new UserBannedException("Пользователь заблокирован");
        }
        throw new SomethingWentWrongException("Что-то пошло не так");
    }

    public User register(RegistrationRequest request) {
        String username = request.getUsername();
        String email = request.getEmail();

        // Проверка на существование аккаунтов с заданными email и username
        if (username != null && (userRepository.getUserByEmail(email).isPresent() ||
                userRepository.getUserByUsername(username).isPresent())) {
            throw new UserAlreadyExistsException("Пользователь с такими данными уже существует!");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .displayName(request.getDisplayName())
                .role(UserRole.USER)
                .status(UserStatus.UNVERIFIED)
                .createdAt(LocalDateTime.now())
                .build();

        User saved = userRepository.save(user);
        sendVerificationToEmail(saved);
        return saved;
    }

    private void sendVerificationToEmail(User user) {
        verificationService.sendVerification(
                new VerificationRequest(VerificationChannel.EMAIL, user.getEmail())
        );
    }

    public Pair<JwtPair, User> verifyEmail(String email, String verificationCode, SessionMetadata metadata) {
        User user = userRepository.getUserByEmail(email).orElseThrow(UserNotFoundException::new);

        Boolean verified = verificationService.verify(
                new VerificationRequest(VerificationChannel.EMAIL, user.getEmail()),
                new VerificationCode(verificationCode, verificationDuration)
        );
        if (!verified) {
            throw new InvalidCredentialsException("Неверно введен код подтверждения");
        }

        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        return Pair.of(createNewSession(user, metadata), user);
    }

    public Pair<JwtPair, User> refreshTokens(String refreshToken, SessionMetadata metadata) {
        if (refreshToken == null) {
            throw new InvalidCredentialsException("Refresh токен не был передан!");
        }
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new InvalidCredentialsException("Невалидный refresh token");
        }
        Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        UUID jti = UUID.fromString(claims.get("jti").toString());

        if (!sessionService.refreshExists(jti)) {
            throw new NotFoundException("Refresh токен не найден");
        }

        SessionDto sessionDto = sessionService.getSession(jti);

        Long userId = sessionDto.getUserId();
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        sessionService.removeRefresh(userId, jti);

        return Pair.of(createNewSession(user, metadata), user);
    }
}

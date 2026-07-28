package ru.n1fex.kinto.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.n1fex.kinto.dto.RegistrationRequest;
import ru.n1fex.kinto.entity.UserRole;
import ru.n1fex.kinto.entity.UserStatus;
import ru.n1fex.kinto.exception.InvalidCredentialsException;
import ru.n1fex.kinto.exception.UserAlreadyExistsException;
import ru.n1fex.kinto.exception.UserNotFoundException;
import ru.n1fex.kinto.entity.User;
import ru.n1fex.kinto.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User login(String login, String password) {
        Optional<User> userOpt = userRepository.getUserByUsernameOrEmail(login, login);

        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден!");
        }
        User user = userOpt.get();
        // todo Проверить аккаунт удален/забанен/неподтвержден
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Неверные данные для входа!");
        }

        // todo Заменить на JwtResponse (сгенерить JWT сохранить refresh в Redis как user:sessions:{user_id}->jti и refresh:jti->bool
        return user;
    }

    public User register(RegistrationRequest request) {
        String username = request.getUsername();
        String email = request.getEmail();

        // Проверка на существование аккаунтов с заданными email и username
        if (username != null && userRepository.getUserByUsernameOrEmail(username, email).isPresent()) {
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

        // Генерация кода подтверждения
        // Задача в rabbitmq на отправку письма на почту с подтверждением
        // todo Отправить в RabbiMQ задачи на отправку письма на почту
        // Сохранение в кода в Redis   verify:mail->code(hashed)  TTL: 10
        // todo Сохранить в Redis код подтверждения почты

        return userRepository.save(user) ;
    }

}

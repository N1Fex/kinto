package ru.n1fex.kinto.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.n1fex.kinto.annotation.PasswordsMatch;
import ru.n1fex.kinto.util.PatternUtils;

@Data
@PasswordsMatch
public class RegistrationRequest {

    @Email(message = "Некорректный формат почты")
    private String email;

    @Pattern(
            regexp = PatternUtils.USERNAME_REGEXP,
            message = "Username должен содержать от 3 до 64 символов и состоять только из латинских букв, цифр и '_'"
    )
    private String username;

    @NotBlank(message = "Имя не может быть пустым или состоять из пробелов")
    @Size(min = 1, max = 64, message = "Имя не может быть пустым и состоять более чем из 64 символов")
    private String displayName;

    @Pattern(regexp = PatternUtils.PASSWORD_REGEXP, message = "Пароль должен состоять минимум из 8 латинских символов: хотя бы 1 маленького, 1 большого и 1 спец.символа")
    private String password;
    private String confirmPassword;
}

package ru.n1fex.kinto.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtRequest {

    @Size(min = 3, max = 254, message = "Некорректная длина почты или имени пользователя")
    private String login;
    @Size(min = 8, max = 64, message = "Некорректная длина пароля")
    private String password;

}

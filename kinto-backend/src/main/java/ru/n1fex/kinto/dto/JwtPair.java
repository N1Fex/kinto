package ru.n1fex.kinto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtPair {

    private String accessToken;
    private String refreshToken;

}

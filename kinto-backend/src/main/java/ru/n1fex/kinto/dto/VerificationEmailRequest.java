package ru.n1fex.kinto.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationEmailRequest {

    private String email;
    private String verificationCode;

}

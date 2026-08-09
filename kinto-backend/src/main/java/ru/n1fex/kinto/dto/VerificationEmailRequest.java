package ru.n1fex.kinto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationEmailRequest {

    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\d{6}$")
    private String verificationCode;

}

package ru.n1fex.kinto.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomVerificationCodeGenerator implements VerificationCodeGenerator {

    private static final String CHARACTERS = "0123456789";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    public String generate() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

}

package ru.n1fex.kinto.service;

import ru.n1fex.kinto.entity.VerificationChannel;

public interface VerificationSender {
    VerificationChannel channel();

    void send(String recipient, String code);
}

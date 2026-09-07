package ru.n1fex.kinto.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.n1fex.kinto.entity.VerificationChannel;

@Component
@RequiredArgsConstructor
public class EmailVerificationSender implements VerificationSender {

    @Value("${spring.mail.from}")
    private String from;

    private final JavaMailSender mailSender;

    @Override
    public VerificationChannel channel() {
        return VerificationChannel.EMAIL;
    }

    @Override
    public void send(String recipient, String code) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(recipient);
        message.setSubject("Kinto Verification Code");
        message.setText(String.format("Ваш код подтверждения почты в Kinto: %s", code));

        mailSender.send(message);
    }
}

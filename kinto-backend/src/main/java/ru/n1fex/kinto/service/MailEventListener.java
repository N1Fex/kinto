package ru.n1fex.kinto.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.n1fex.kinto.dto.VerificationRequestEvent;

@Component
@RequiredArgsConstructor
public class MailEventListener {

    private final VerificationSender verificationSender;

    @RabbitListener(
            queues = "mail.verification.queue"
    )
    public void handle(VerificationRequestEvent event) {

        //verificationSender.send(event.getRecipient(), event.getCode());

        System.out.println(
                "Send email to " + event.getRecipient() + ". The code is " + event.getCode()
        );

    }
}

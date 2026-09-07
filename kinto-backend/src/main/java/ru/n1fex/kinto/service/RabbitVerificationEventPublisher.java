package ru.n1fex.kinto.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.n1fex.kinto.dto.VerificationRequestEvent;

@Service
@RequiredArgsConstructor
public class RabbitVerificationEventPublisher implements VerificationEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final VerificationRoutingKeyResolver routingKeyResolver;

    @Override
    public void publish(VerificationRequestEvent event) {

        rabbitTemplate.convertAndSend(
                "verification.exchange",
                routingKeyResolver.resolve(event.getChannel()),
                event
        );
    }

}

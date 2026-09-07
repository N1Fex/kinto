package ru.n1fex.kinto.service;

import ru.n1fex.kinto.dto.VerificationRequestEvent;

public interface VerificationEventPublisher {

    void publish(VerificationRequestEvent event);

}

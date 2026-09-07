package ru.n1fex.kinto.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.n1fex.kinto.dto.VerificationCode;
import ru.n1fex.kinto.dto.VerificationRequest;
import ru.n1fex.kinto.dto.VerificationRequestEvent;
import ru.n1fex.kinto.exception.NotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationStorage storage;
    private final VerificationEventPublisher publisher;
    private final VerificationCodeGenerator generator;

    public void sendVerification(VerificationRequest request) {

        String code = generator.generate();

        storage.save(request, new VerificationCode(code));

        publisher.publish(
                new VerificationRequestEvent(
                        request.getChannel(),
                        request.getRecipient(),
                        code
                )
        );
    }

    public Boolean verify(VerificationRequest request, VerificationCode code) {

        Optional<VerificationCode> savedCode = storage.get(request);
        Boolean eq = code.equals(savedCode.orElseThrow(() -> new NotFoundException("Email уже подтвержден, либо письмо не было отправлено")));

        if (eq) {
            storage.delete(request);
        }

        return eq;

    }

}

package ru.n1fex.kinto.service;

import org.springframework.stereotype.Component;
import ru.n1fex.kinto.entity.VerificationChannel;

@Component
public class VerificationRoutingKeyResolver {

    public String resolve(
            VerificationChannel channel
    ) {

        return switch(channel) {

            case EMAIL ->
                    "verification.email";

            case PHONE ->
                    "verification.phone";

        };
    }
}
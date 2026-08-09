package ru.n1fex.kinto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.n1fex.kinto.entity.VerificationChannel;

@Data
@AllArgsConstructor
public class VerificationRequestEvent {
    private VerificationChannel channel;
    private String recipient;
    private String code;
}

package ru.n1fex.kinto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class SessionDto {
    private Long userId;
    private UUID sid;
    private String device;
    private String country;
    private String browser;
    private String os;
    private String ip;
    private Instant createdAt;
    private Instant expiresAt;
}

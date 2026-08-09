package ru.n1fex.kinto.service;

import ru.n1fex.kinto.dto.VerificationCode;
import ru.n1fex.kinto.dto.VerificationRequest;

import java.util.Optional;

public interface VerificationStorage {

    void save(VerificationRequest request, VerificationCode code);
    Optional<VerificationCode> get(VerificationRequest request);
    void delete(VerificationRequest request);

}

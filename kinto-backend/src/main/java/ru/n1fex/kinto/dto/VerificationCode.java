package ru.n1fex.kinto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.util.Objects;

@Data
@AllArgsConstructor
public class VerificationCode {
    String code;
    Duration ttl = Duration.ofDays(1);

    public VerificationCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VerificationCode that = (VerificationCode) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}

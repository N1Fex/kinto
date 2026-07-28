package ru.n1fex.kinto.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class ErrorResponse {
    private final int status;
    private final String code;
    private final String message;
    private final Date timestamp;

    public ErrorResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.timestamp = new Date();
    }
}

package ru.n1fex.kinto.exception;

public class SomethingWentWrongException extends RuntimeException {
    public SomethingWentWrongException() {
        super("Что-то пошло не так");
    }
    public SomethingWentWrongException(String message) {
        super(message);
    }
}

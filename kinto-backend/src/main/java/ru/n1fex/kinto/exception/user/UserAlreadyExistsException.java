package ru.n1fex.kinto.exception.user;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("Пользователь уже существует");
    }
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

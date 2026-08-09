package ru.n1fex.kinto.exception.user;

public class UserUnverifiedException extends RuntimeException {
    public UserUnverifiedException() {
        super("Почта пользователя не подтверждена");
    }
    public UserUnverifiedException(String message) {
        super(message);
    }
}

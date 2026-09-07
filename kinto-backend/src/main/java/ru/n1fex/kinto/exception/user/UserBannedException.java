package ru.n1fex.kinto.exception.user;

public class UserBannedException extends RuntimeException {
    public UserBannedException() {
        super("Пользователь заблокирован");
    }
    public UserBannedException(String message) {
        super(message);
    }
}

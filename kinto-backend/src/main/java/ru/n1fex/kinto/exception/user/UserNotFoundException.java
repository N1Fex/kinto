package ru.n1fex.kinto.exception.user;


import ru.n1fex.kinto.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super("Пользователь не найден");
    }
    public UserNotFoundException(String message) {
        super(message);
    }
}

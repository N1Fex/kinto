package ru.n1fex.kinto.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.n1fex.kinto.exception.InvalidCredentialsException;
import ru.n1fex.kinto.exception.NotFoundException;
import ru.n1fex.kinto.exception.SomethingWentWrongException;
import ru.n1fex.kinto.exception.user.UserAlreadyExistsException;
import ru.n1fex.kinto.exception.user.UserBannedException;
import ru.n1fex.kinto.exception.user.UserNotFoundException;
import ru.n1fex.kinto.dto.ErrorResponse;
import ru.n1fex.kinto.exception.user.UserUnverifiedException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(UserNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "USER_NOT_FOUND",
                !ex.getMessage().isEmpty() ? ex.getMessage() : "Пользователь не найден"
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidCredentialsException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_CREDENTIALS",
                !ex.getMessage().isEmpty() ? ex.getMessage() : "Неверные данные для входа"
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleException(UserAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "USER_ALREADY_EXISTS",
                !ex.getMessage().isEmpty() ? ex.getMessage() : "Пользователь уже существует"
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "BAD_REQUEST",
                (msg != null && !msg.isEmpty()) ? msg : "Неверное тело запроса"
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UserBannedException.class)
    public ResponseEntity<ErrorResponse> handleException(UserBannedException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "USER_BANNED",
                !ex.getMessage().isEmpty() ? ex.getMessage() : "Пользователь заблокирован"
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserUnverifiedException.class)
    public ResponseEntity<ErrorResponse> handleException(UserUnverifiedException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "EMAIL_NOT_VERIFIED",
                !ex.getMessage().isEmpty() ? ex.getMessage() : "Почта не подтверждена"
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(NotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "NOT_FOUND",
                !ex.getMessage().isEmpty() ? ex.getMessage() : "Ресурс не был найден"
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(NoResourceFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "NOT_FOUND",
                !ex.getMessage().isEmpty() ? ex.getMessage() : "Ресурс не был найден"
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SomethingWentWrongException.class)
    public ResponseEntity<ErrorResponse> handleException(SomethingWentWrongException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "SOMETHING_WENT_WRONG",
                !ex.getMessage().isEmpty() ? ex.getMessage() : "Что-то пошло не так"
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "Произошла неизвестная ошибка"
        );
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

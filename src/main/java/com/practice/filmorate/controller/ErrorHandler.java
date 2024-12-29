package com.practice.filmorate.controller;

import com.practice.filmorate.exceptions.NotFoundException;
import com.practice.filmorate.exceptions.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
    // можно сюда логирование warn
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(ValidationException e) {
        return new ErrorResponse("Ошибка валидации.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(NotFoundException e) {
        return new ErrorResponse("Объект не найден.", e.getMessage());
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse handle(Throwable e) {
//        return new ErrorResponse("Ошибка сервера.", e.getMessage());
//    }
}

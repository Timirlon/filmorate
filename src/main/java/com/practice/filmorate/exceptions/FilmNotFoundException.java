package com.practice.filmorate.exceptions;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException() {
        super();
    }

    public FilmNotFoundException(String message) {
        super(message);
    }
}

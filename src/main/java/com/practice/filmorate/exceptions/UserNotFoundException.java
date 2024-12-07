package com.practice.filmorate.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}

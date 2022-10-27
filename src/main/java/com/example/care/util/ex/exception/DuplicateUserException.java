package com.example.care.util.ex.exception;

public class DuplicateUserException extends RuntimeException{
    public DuplicateUserException() {
    }

    public DuplicateUserException(String message) {
        super(message);
    }
}

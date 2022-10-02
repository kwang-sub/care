package com.example.care.util.ex.exception;

public class UserAccessException extends RuntimeException{
    public UserAccessException() {
        super();
    }

    public UserAccessException(String message) {
        super(message);
    }
}

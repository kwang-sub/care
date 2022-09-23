package com.example.care.util.exception;

public class ReserveFullException extends RuntimeException {
    public ReserveFullException() {
        super();
    }

    public ReserveFullException(String message) {
        super(message);
    }
}

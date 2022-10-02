package com.example.care.util.ex.exception;

public class RequestParamBindException extends RuntimeException {
    public RequestParamBindException() {
        super();
    }

    public RequestParamBindException(String message) {
        super(message);
    }
}

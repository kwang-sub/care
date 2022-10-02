package com.example.care.util.ex.exception;

public class DuplicateMembershipException extends RuntimeException {
    public DuplicateMembershipException() {
        super();
    }

    public DuplicateMembershipException(String message) {
        super(message);
    }
}

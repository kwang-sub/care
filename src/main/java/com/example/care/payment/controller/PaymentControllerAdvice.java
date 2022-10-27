package com.example.care.payment.controller;

import com.example.care.util.ex.exception.DuplicateMembershipException;
import com.example.care.util.ex.exception.UserAccessException;
import com.example.care.util.ex.handler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(assignableTypes = {PaymentController.class})
public class PaymentControllerAdvice {

    @ExceptionHandler(DuplicateMembershipException.class)
    public ResponseEntity<ErrorResult> dupMembershipHandler(DuplicateMembershipException e) {
        log.error("[ExceptionHandler] ", e);
        return new ResponseEntity(new ErrorResult("400", e.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAccessException.class)
    public ResponseEntity<ErrorResult> userAccessHandler(UserAccessException e) {
        log.error("[ExceptionHandler] ", e);
        return new ResponseEntity(new ErrorResult("401", e.getMessage()),HttpStatus.UNAUTHORIZED);
    }
}

package com.example.care.reply.controller;

import com.example.care.util.ex.exception.RequestParamBindException;
import com.example.care.util.ex.exception.UserAccessException;
import com.example.care.util.ex.handler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = {ReplyController.class})
public class ReplyControllerAdvice {

    @ExceptionHandler(UserAccessException.class)
    public ResponseEntity<ErrorResult> userAccessExHandler(UserAccessException e) {
        log.error("[exceptionHandler] ", e);
        ErrorResult errorResult = new ErrorResult("403", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RequestParamBindException.class)
    public ResponseEntity<ErrorResult> reqParamBindExHandler(RequestParamBindException e) {
        log.error("[exceptionHandler] ", e);
        ErrorResult errorResult = new ErrorResult("400", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }
}

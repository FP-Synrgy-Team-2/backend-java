package com.example.jangkau.utils;

import com.example.jangkau.dto.base.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandlers {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<String>> constraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.failure(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<BaseResponse<String>> apiException(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getRawStatusCode())
                .body(BaseResponse.failure(exception.getRawStatusCode(), exception.getReason()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> usernameNotFoundException(UsernameNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.failure(HttpStatus.NOT_FOUND.value(), exception.getMessage()));
    }
}

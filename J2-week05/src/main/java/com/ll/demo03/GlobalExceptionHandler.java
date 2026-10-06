package com.ll.demo03;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<RsData<Void>> handle(GlobalException exception) {
        return ResponseEntity.status(exception.getRsData().statusCode())
                .body(exception.getRsData());
    }
}

package com.ll.demo03;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    public RsData<Void> handle(GlobalException exception) { return exception.getRsData(); }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public RsData<Void> conflict(DataIntegrityViolationException exception) {
        return RsData.failure(409, "이미 존재하거나 저장 조건에 맞지 않는 회원 정보입니다.");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .sorted().findFirst().orElse("입력값을 확인해 주세요.");
        return ResponseEntity.badRequest().body(RsData.failure(400, message));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>(RsData.failure(status.value(), "요청을 처리할 수 없습니다."), headers, status);
    }

    @ExceptionHandler(Exception.class)
    public RsData<Void> unexpected(Exception exception) {
        logger.error("Unexpected request failure", exception);
        return RsData.failure(500, "서버 오류가 발생했습니다.");
    }
}

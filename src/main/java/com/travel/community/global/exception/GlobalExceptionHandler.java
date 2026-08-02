package com.travel.community.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.travel.community.global.exception.dto.BusinessException;
import com.travel.community.global.exception.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    /**
     * 비지니스 로직 커스텀 exception
     * 
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("BusinessException Error URI : {}", request.getRequestURI(), e);
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode().getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(errorResponse);
    }

    /**
     * 비즈니스 로직 실행 중 오류 발생 (RuntimeException - 일반적인 비즈니스 예외)
     */
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e, HttpServletRequest request) {

        log.error("RuntimeException at URL: {} - error:", request.getRequestURI(), e);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}

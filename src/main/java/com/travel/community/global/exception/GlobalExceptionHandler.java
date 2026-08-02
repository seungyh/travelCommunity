package com.travel.community.global.exception;

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
}

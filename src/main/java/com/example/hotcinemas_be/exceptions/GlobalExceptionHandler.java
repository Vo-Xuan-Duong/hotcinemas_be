package com.example.hotcinemas_be.exceptions;

import com.example.hotcinemas_be.dtos.ResponseData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler{

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        // Log the exception (optional)
         log.error("Runtime exception occurred: {}", ex.getMessage(), ex);
        ResponseData<?> error = ResponseData.builder()
                .status(500)
                .message("An unexpected error occurred: " + ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        // Return a generic error response
        return ResponseEntity.status(500).body(error);
    }

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<?> handleErrorException(ErrorException ex, HttpServletRequest request) {
        // Log the exception (optional)
        ErrorCode errorCode = ex.getErrorCode();

        log.error("Error exception occurred: {}", ex.getMessage(), ex);
        ResponseData<?> error = ResponseData.builder()
                .status(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        // Return the error response with the specific status code
        return ResponseEntity.status(errorCode.getHttpStatus()).body(error);
    }
}

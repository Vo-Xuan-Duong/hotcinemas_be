package com.example.hotcinemas_be.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    ERROR_UNCATEGORIZED("CINEMA_001", "Error Unauthorized Access", HttpStatus.INTERNAL_SERVER_ERROR),
    ERROR_INVALID_REQUEST("CINEMA_002", "Invalid request", HttpStatus.BAD_REQUEST),
    ERROR_BAD_CREDENTIALS("CINEMA_003", "Bad credentials", HttpStatus.UNAUTHORIZED),



    ERROR_MODEL_NOT_FOUND("CINEMA_010", "User not found", HttpStatus.NOT_FOUND),
    ERROR_MODEL_ALREADY_EXISTS("CINEMA_011", "User already exists", HttpStatus.CONFLICT),
    PASSWORD_NOT_MATCH("CINEMA_012", "Password does not match", HttpStatus.BAD_REQUEST),
    CONFIRM_PASSWORD_AND_PASSWORD_NOT_MATCH("CINEMA_013", "Confirm password and password does not match", HttpStatus.BAD_REQUEST),


    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

}

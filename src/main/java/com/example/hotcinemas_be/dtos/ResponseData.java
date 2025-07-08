package com.example.hotcinemas_be.dtos;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData<T> extends ResponseEntity<T> {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private T data;

    @Builder
    public ResponseData(T data, int status, String message, LocalDateTime timestamp, String path, org.springframework.http.HttpStatus httpStatus) {
        super(data, httpStatus);
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
        this.data = data;
    }
}

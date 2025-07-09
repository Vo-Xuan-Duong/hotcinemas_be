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
@Builder
public class ResponseData<T>{
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private T data;

}

package com.example.hotcinemas_be.dtos.cinema.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CinemaResponse {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String city;
    private String createdAt;
    private String updatedAt;
}


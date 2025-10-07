package com.example.hotcinemas_be.mappers;

import com.example.hotcinemas_be.dtos.cinema.responses.CinemaResponse;
import com.example.hotcinemas_be.models.Cinema;
import org.springframework.stereotype.Service;

@Service
public class CinemaMapper {
    public CinemaResponse mapToResponse(Cinema cinema) {
        if (cinema == null) {
            return null;
        }
        return CinemaResponse.builder()
                .id(cinema.getId())
                .name(cinema.getName())
                .address(cinema.getAddress())
                .phoneNumber(cinema.getPhoneNumber())
                .city(cinema.getCity())
                .createdAt(cinema.getCreatedAt() != null ? cinema.getCreatedAt().toString() : null)
                .updatedAt(cinema.getUpdatedAt() != null ? cinema.getUpdatedAt().toString() : null)
                .build();
    }
}

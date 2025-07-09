package com.example.hotcinemas_be.mappers;

import com.example.hotcinemas_be.dtos.responses.CinemaResponse;
import com.example.hotcinemas_be.models.Cinema;
import org.springframework.stereotype.Service;

@Service
public class CinemaMapper {
    public CinemaResponse mapToResponse(Cinema cinema) {
        if (cinema == null) {
            return null;
        }
        return CinemaResponse.builder()
                .cinemaId(cinema.getCinemaId())
                .name(cinema.getName())
                .address(cinema.getAddress())
                .phoneNumber(cinema.getPhoneNumber())
                .city(cinema.getCity())
                .createdAt(cinema.getCreatedAt().toString())
                .updatedAt(cinema.getUpdatedAt().toString())
                .build();
    }
}

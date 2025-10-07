package com.example.hotcinemas_be.services;

import com.example.hotcinemas_be.dtos.cinema.requests.CinemaRequest;
import com.example.hotcinemas_be.dtos.cinema.responses.CinemaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CinemaService {
    public CinemaResponse createCinema(CinemaRequest cinemaRequest);

    public CinemaResponse getCinemaById(Long cinemaId);

    public CinemaResponse updateCinema(Long cinemaId, CinemaRequest cinemaRequest);

    public void deleteCinema(Long cinemaId);

    public CinemaResponse getCinemaByName(String name);

    public Page<CinemaResponse> getAllCinemas(Pageable pageable);

    public Page<CinemaResponse> searchCinemas(String keyword, Pageable pageable);

    public Page<CinemaResponse> getCinemasByCity(String city, Pageable pageable);
}

package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.dtos.requests.CinemaRequest;
import com.example.hotcinemas_be.dtos.responses.CinemaResponse;
import com.example.hotcinemas_be.mappers.CinemaMapper;
import com.example.hotcinemas_be.models.Cinema;
import com.example.hotcinemas_be.repositorys.CinemaRepository;
import com.example.hotcinemas_be.services.CinemaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CinemaServiceImpl implements CinemaService {

    private final CinemaMapper cinemaMapper;
    private final CinemaRepository cinemaRepository;

    public CinemaServiceImpl(CinemaMapper cinemaMapper, CinemaRepository cinemaRepository) {
        this.cinemaMapper = cinemaMapper;
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public CinemaResponse createCinema(CinemaRequest cinemaRequest) {
        Cinema cinema = new Cinema();
        cinema.setName(cinemaRequest.getName());
        cinema.setAddress(cinemaRequest.getAddress());
        cinema.setPhoneNumber(cinemaRequest.getPhoneNumber());
        cinema.setCity(cinemaRequest.getCity());

        return cinemaMapper.mapToResponse(cinemaRepository.save(cinema));
    }

    @Override
    public CinemaResponse getCinemaById(Long cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new RuntimeException("Cinema not found with id: " + cinemaId));

        return cinemaMapper.mapToResponse(cinema);
    }

    @Override
    public CinemaResponse updateCinema(Long cinemaId, CinemaRequest cinemaRequest) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new RuntimeException("Cinema not found with id: " + cinemaId));

        cinema.setName(cinemaRequest.getName());
        cinema.setAddress(cinemaRequest.getAddress());
        cinema.setPhoneNumber(cinemaRequest.getPhoneNumber());
        cinema.setCity(cinemaRequest.getCity());

        return cinemaMapper.mapToResponse(cinemaRepository.save(cinema));
    }

    @Override
    public void deleteCinema(Long cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new RuntimeException("Cinema not found with id: " + cinemaId));
        cinemaRepository.delete(cinema);
    }

    @Override
    public CinemaResponse getCinemaByName(String name) {
        Cinema cinema = cinemaRepository.findCinemaByName(name)
                .orElseThrow(() -> new RuntimeException("Cinema not found with name: " + name));

        return cinemaMapper.mapToResponse(cinema);
    }

    @Override
    public Page<CinemaResponse> getAllCinemas(Pageable pageable) {
        Page<Cinema> cinemas = cinemaRepository.findAll(pageable);
        return cinemas.map(cinemaMapper::mapToResponse);
    }

    @Override
    public Page<CinemaResponse> searchCinemas(String keyword, Pageable pageable) {
        Page<Cinema> cinemas = cinemaRepository.findAll(pageable);
        return (Page<CinemaResponse>) cinemas.map(cinema -> {
            if (cinema.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                cinema.getAddress().toLowerCase().contains(keyword.toLowerCase()) ||
                cinema.getCity().toLowerCase().contains(keyword.toLowerCase())) {
                return cinemaMapper.mapToResponse(cinema);
            }
            return null; // Filter out non-matching cinemas
        }).filter(Objects::nonNull);
    }

    @Override
    public Page<CinemaResponse> getCinemasByCity(String city, Pageable pageable) {
        Page<Cinema> cinemas = cinemaRepository.findCinemasByCity(city, pageable);
        if (cinemas.isEmpty()) {
            throw new RuntimeException("No cinemas found in the city: " + city);
        }
        return cinemas.map(cinemaMapper::mapToResponse);
    }
}

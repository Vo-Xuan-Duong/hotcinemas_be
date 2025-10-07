package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.dtos.cinema.requests.CinemaRequest;
import com.example.hotcinemas_be.dtos.cinema.responses.CinemaResponse;
import com.example.hotcinemas_be.mappers.CinemaMapper;
import com.example.hotcinemas_be.models.Cinema;
import com.example.hotcinemas_be.repositorys.CinemaRepository;
import com.example.hotcinemas_be.services.CinemaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CinemaServiceImpl implements CinemaService {

    private final CinemaMapper cinemaMapper;
    private final CinemaRepository cinemaRepository;

    public CinemaServiceImpl(CinemaMapper cinemaMapper, CinemaRepository cinemaRepository) {
        this.cinemaMapper = cinemaMapper;
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public CinemaResponse createCinema(CinemaRequest cinemaRequest) {
        // Check if cinema with same name already exists
        if (cinemaRepository.findByName(cinemaRequest.getName()).isPresent()) {
            throw new RuntimeException("Cinema with name '" + cinemaRequest.getName() + "' already exists");
        }

        Cinema cinema = Cinema.builder()
                .name(cinemaRequest.getName())
                .address(cinemaRequest.getAddress())
                .phoneNumber(cinemaRequest.getPhoneNumber())
                .city(cinemaRequest.getCity())
                .isActive(true)
                .build();

        Cinema savedCinema = cinemaRepository.save(cinema);
        return cinemaMapper.mapToResponse(savedCinema);
    }

    @Override
    @Transactional(readOnly = true)
    public CinemaResponse getCinemaById(Long cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new RuntimeException("Cinema not found with id: " + cinemaId));

        return cinemaMapper.mapToResponse(cinema);
    }

    @Override
    public CinemaResponse updateCinema(Long cinemaId, CinemaRequest cinemaRequest) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new RuntimeException("Cinema not found with id: " + cinemaId));

        // Check if another cinema with same name exists (excluding current cinema)
        cinemaRepository.findByName(cinemaRequest.getName())
                .ifPresent(existingCinema -> {
                    if (!existingCinema.getId().equals(cinemaId)) {
                        throw new RuntimeException("Cinema with name '" + cinemaRequest.getName() + "' already exists");
                    }
                });

        cinema.setName(cinemaRequest.getName());
        cinema.setAddress(cinemaRequest.getAddress());
        cinema.setPhoneNumber(cinemaRequest.getPhoneNumber());
        cinema.setCity(cinemaRequest.getCity());

        Cinema updatedCinema = cinemaRepository.save(cinema);
        return cinemaMapper.mapToResponse(updatedCinema);
    }

    @Override
    public void deleteCinema(Long cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new RuntimeException("Cinema not found with id: " + cinemaId));

        // Soft delete by setting isActive to false
        cinema.setIsActive(false);
        cinemaRepository.save(cinema);
    }

    @Override
    @Transactional(readOnly = true)
    public CinemaResponse getCinemaByName(String name) {
        Cinema cinema = cinemaRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Cinema not found with name: " + name));

        return cinemaMapper.mapToResponse(cinema);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CinemaResponse> getAllCinemas(Pageable pageable) {
        Page<Cinema> cinemas = cinemaRepository.findByIsActiveTrue(pageable);
        return cinemas.map(cinemaMapper::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CinemaResponse> searchCinemas(String keyword, Pageable pageable) {
        Page<Cinema> cinemas = cinemaRepository.searchCinemas(keyword, pageable);
        return cinemas.map(cinemaMapper::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CinemaResponse> getCinemasByCity(String city, Pageable pageable) {
        Page<Cinema> cinemas = cinemaRepository.findByCity(city, pageable);
        return cinemas.map(cinemaMapper::mapToResponse);
    }
}

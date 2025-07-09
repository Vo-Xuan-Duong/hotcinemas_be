package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.config.OpenApiConfig;
import com.example.hotcinemas_be.models.Cinema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CinemaRepository extends JpaRepository<Cinema,Long> {
    Optional<Cinema> findCinemaByCinemaId(Long cinemaId);
    Optional<Cinema> findCinemaByName(String name);
    Page<Cinema> findCinemasByCity(String city, Pageable pageable);
}

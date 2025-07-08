package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.models.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinemaRepository extends JpaRepository<Cinema,Long> {
}

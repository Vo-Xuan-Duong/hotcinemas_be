package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat,Long> {
}

package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat,Long> {
    List<Seat> findAllByRoom_RoomId(Long roomId);
}

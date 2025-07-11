package com.example.hotcinemas_be.services;


import com.example.hotcinemas_be.enums.SeatStatus;

import java.time.LocalDateTime;

public interface ShowtimeSeatService {
    public void createShowtimeSeats(Long ShowtimeId);
    public void deleteShowtimeSeats(Long showtimeId);
    public boolean updateShowtimeSeatHeld(Long showtimeSeatId, String status, Long userId, LocalDateTime heldUntil);
    public boolean updateShowtimeSeatStatus(Long showtimeSeatId, SeatStatus status);
}

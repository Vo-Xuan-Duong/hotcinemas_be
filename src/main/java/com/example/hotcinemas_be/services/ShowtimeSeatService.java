package com.example.hotcinemas_be.services;

import com.example.hotcinemas_be.dtos.showtime_seat.responses.ShowtimeSeatResponse;
import com.example.hotcinemas_be.enums.SeatStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowtimeSeatService {
    public void createShowtimeSeats(Long ShowtimeId);

    public void deleteShowtimeSeats(Long showtimeId);

    public boolean updateShowtimeSeatHeld(Long showtimeSeatId, String status, Long userId, LocalDateTime heldUntil);

    public boolean updateShowtimeSeatStatus(Long showtimeSeatId, SeatStatus status, Long userId);

    public List<ShowtimeSeatResponse> getShowtimeSeatsByShowtimeId(Long showtimeId);

    void changeStatusSeatByStaff(Long seatId, SeatStatus status);
}

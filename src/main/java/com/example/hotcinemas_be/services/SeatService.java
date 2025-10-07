package com.example.hotcinemas_be.services;

import com.example.hotcinemas_be.dtos.seat.requests.SeatRequest;
import com.example.hotcinemas_be.dtos.seat.responses.SeatResponse;
import com.example.hotcinemas_be.enums.SeatType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SeatService {
    SeatResponse createSeat(SeatRequest seatRequest);

    SeatResponse getSeatById(Long seatId);

    SeatResponse updateSeat(Long seatId, SeatRequest seatRequest);

    void deleteSeat(Long seatId);

    List<SeatResponse> getSeatsByRoomId(Long roomId);

    List<SeatResponse> getSeatsByRoomIdAndActive(Long roomId);

    List<SeatResponse> getSeatsBySeatType(SeatType seatType);

    List<SeatResponse> getSeatsByRoomIdAndSeatType(Long roomId, SeatType seatType);

    List<SeatResponse> getSeatsByCinemaId(Long cinemaId);

    SeatResponse getSeatByRoomAndPosition(Long roomId, String rowLabel, String seatNumber);

    void createSeatsForRoom(Long roomId, Integer rowsCount, Integer seatsPerRow);

    void deleteSeatsByRoomId(Long roomId);

    SeatResponse activateSeat(Long seatId);

    SeatResponse deactivateSeat(Long seatId);
}

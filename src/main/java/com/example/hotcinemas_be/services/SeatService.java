package com.example.hotcinemas_be.services;


import com.example.hotcinemas_be.dtos.requests.SeatRequest;
import com.example.hotcinemas_be.dtos.responses.SeatResponse;
import com.example.hotcinemas_be.models.Room;

import java.util.List;

public interface SeatService {
    public void createSeatsForRoom(Room room, Integer rowsCount, Integer seatsPerRow, Double priceMultiplier);
    public void deleteSeatsByRoomId(Long roomId);
    public void deleteSeatById(Long seatId);
    public SeatResponse addSeatToRoom(Long roomId, SeatRequest seatRequest);
    public SeatResponse updateSeat(Long seatId, SeatRequest seatRequest);
    public List<SeatResponse> getSeatsByRoomId(Long roomId);
}

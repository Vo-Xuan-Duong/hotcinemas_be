package com.example.hotcinemas_be.mappers;

import com.example.hotcinemas_be.dtos.responses.SeatResponse;
import com.example.hotcinemas_be.models.Seat;
import org.springframework.stereotype.Service;

@Service
public class SeatMapper {
    public SeatResponse mapToResponse(Seat seat) {
        if (seat == null) {
            return null;
        }
        return SeatResponse.builder()
                .seatId(seat.getSeatId())
                .rowNumber(seat.getRowNumber())
                .seatNumber(seat.getSeatNumber())
                .isPhysicalAvailable(seat.getIsPhysicalAvailable())
                .build();
    }
}

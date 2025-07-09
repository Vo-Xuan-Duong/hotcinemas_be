package com.example.hotcinemas_be.mappers;

import com.example.hotcinemas_be.dtos.responses.ShowtimeSeatResponse;
import com.example.hotcinemas_be.models.ShowtimeSeat;
import org.springframework.stereotype.Service;

@Service
public class ShowtimeSeatMapper {
    public ShowtimeSeatResponse mapToResponse(ShowtimeSeat showtimeSeat) {
        if (showtimeSeat == null) {
            return null;
        }

        return ShowtimeSeatResponse.builder()
                .showtimeSeatId(showtimeSeat.getShowtimeSeatId())
                .showtimeId(showtimeSeat.getShowtime().getShowtimeId())
                .seatId(showtimeSeat.getSeat().getSeatId())
                .rowNumber(showtimeSeat.getSeat().getRowNumber())
                .seatNumber(showtimeSeat.getSeat().getSeatNumber())
                .seatType(showtimeSeat.getSeat().getSeatType())
                .status(showtimeSeat.getStatus().name())
                .heldByUserId(showtimeSeat.getHeldByUser() != null ? showtimeSeat.getHeldByUser().getUserId() : null)
                .heldUntil(showtimeSeat.getHeldUntil() != null ? showtimeSeat.getHeldUntil() : null)
                .build();
    }
}

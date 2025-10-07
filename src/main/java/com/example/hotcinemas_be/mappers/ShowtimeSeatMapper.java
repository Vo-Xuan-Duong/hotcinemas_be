package com.example.hotcinemas_be.mappers;

import com.example.hotcinemas_be.dtos.showtime_seat.responses.ShowtimeSeatResponse;
import com.example.hotcinemas_be.models.ShowtimeSeat;
import org.springframework.stereotype.Service;

@Service
public class ShowtimeSeatMapper {
    public ShowtimeSeatResponse mapToResponse(ShowtimeSeat showtimeSeat) {
        if (showtimeSeat == null) {
            return null;
        }

        return ShowtimeSeatResponse.builder()
                .id(showtimeSeat.getId())
                .showtimeId(showtimeSeat.getShowtime().getId())
                .seatId(showtimeSeat.getSeat().getId())
                .rowLabel(showtimeSeat.getSeat().getRowLabel())
                .seatNumber(showtimeSeat.getSeat().getSeatNumber())
                .col(showtimeSeat.getSeat().getCol())
                .row(showtimeSeat.getSeat().getRow())
                .seatType(showtimeSeat.getSeat().getSeatType())
                .status(showtimeSeat.getStatus().name())
                .heldByUserId(showtimeSeat.getHeldByUser() != null ? showtimeSeat.getHeldByUser().getId() : null)
                .heldUntil(showtimeSeat.getHeldUntil() != null ? showtimeSeat.getHeldUntil() : null)
                .build();
    }
}

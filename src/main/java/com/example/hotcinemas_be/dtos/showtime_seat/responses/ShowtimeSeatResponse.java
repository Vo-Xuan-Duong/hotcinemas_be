package com.example.hotcinemas_be.dtos.showtime_seat.responses;

import com.example.hotcinemas_be.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowtimeSeatResponse {
    private Long id;
    private Long showtimeId;
    private Long seatId;
    private String rowLabel;
    private String seatNumber;
    private Integer col;
    private Integer row;
    private SeatType seatType;
    private String status;
    private Long heldByUserId;
    private LocalDateTime heldUntil;
}


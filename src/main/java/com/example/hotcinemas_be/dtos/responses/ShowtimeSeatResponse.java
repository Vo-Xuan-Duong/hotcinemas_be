package com.example.hotcinemas_be.dtos.responses;

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
    private Long showtimeSeatId; // ID of the showtime seat
    private Long showtimeId; // ID of the showtime
    private Long seatId; // ID of the seat
    private String rowNumber; // Row number of the seat
    private Integer seatNumber; // Seat number within the row
    private SeatType seatType; // Type of the seat (e.g., "NORMAL", "VIP", "ACCESSIBLE")
    private String status; // Status of the seat (e.g., "AVAILABLE", "HELD", "BOOKED")
    private Long heldByUserId; // ID of the user who holds the seat, if applicable
    private LocalDateTime heldUntil; // ISO-8601 format for held until time, if applicable

}

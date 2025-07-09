package com.example.hotcinemas_be.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowtimeResponse {
    private Long showtimeId; // ID of the showtime
    private String movieTitle; // Title of the movie
    private String roomName; // Name of the room
    private String startTime; // Start time in ISO-8601 format (e.g., "2023-10-01T14:30:00")
    private String endTime; // End time in ISO-8601 format (e.g., "2023-10-01T16:30:00")
    private BigDecimal ticketPrice; // Ticket price as a double
    private Boolean isActive = true; // Default to true if not provided
}

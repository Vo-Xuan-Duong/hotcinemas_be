package com.example.hotcinemas_be.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowtimeRequest {
    private Long movieId; // ID of the movie
    private LocalDateTime startTime; // Start time in ISO-8601 format (e.g., "2023-10-01T14:30:00")
//    private LocalDateTime endTime; // End time in ISO-8601 format (e.g., "2023-10-01T16:30:00")
    private Double ticketPrice; // Ticket price as a double
    private Boolean isActive = true; // Default to true if not provided
}

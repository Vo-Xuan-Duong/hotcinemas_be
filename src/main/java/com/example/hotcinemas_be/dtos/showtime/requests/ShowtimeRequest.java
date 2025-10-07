package com.example.hotcinemas_be.dtos.showtime.requests;

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
    private Long roomId;
    private Long movieId;
    private LocalDateTime startTime;
    private Double ticketPrice;
    private Boolean isActive;
}

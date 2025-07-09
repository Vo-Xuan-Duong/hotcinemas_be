package com.example.hotcinemas_be.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatResponse {
    private Long seatId;
    private String rowNumber;
    private Integer seatNumber;
    private String seatType; // e.g., "Regular", "VIP", "Disabled"
    private Boolean isPhysicalAvailable; // Indicates if the seat is available for booking
}

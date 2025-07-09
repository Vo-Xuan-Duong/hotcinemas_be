package com.example.hotcinemas_be.dtos.requests;

import com.example.hotcinemas_be.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatRequest {
    private String rowNumber;
    private Integer seatNumber;
    private SeatType seatType; // e.g., "Regular", "VIP", "Disabled"
}

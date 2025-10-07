package com.example.hotcinemas_be.dtos.booking.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    private Long showtimeId;
    private List<Long> seatIds;
    private String promotionalCode;
}


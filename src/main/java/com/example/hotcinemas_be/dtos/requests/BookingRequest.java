package com.example.hotcinemas_be.dtos.requests;

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
    private List<Long> seatIds; // List of seat IDs to book
    private String promotionalCode; // Optional promotional code
}

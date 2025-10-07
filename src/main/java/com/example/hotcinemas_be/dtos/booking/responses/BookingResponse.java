package com.example.hotcinemas_be.dtos.booking.responses;

import com.example.hotcinemas_be.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private String bookingCode;
    private BookingStatus status;
    private String createdAt;
    private String updatedAt;
    private Long userId;
    private Long showtimeId;
    private BigDecimal totalPrice;
    private Integer totalSeats;
    private String seats;
}










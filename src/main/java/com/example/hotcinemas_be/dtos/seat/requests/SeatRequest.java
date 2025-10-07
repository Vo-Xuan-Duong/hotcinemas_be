package com.example.hotcinemas_be.dtos.seat.requests;

import com.example.hotcinemas_be.enums.SeatStatus;
import com.example.hotcinemas_be.enums.SeatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatRequest {

    @NotNull(message = "Room ID is required")
    private Long roomId;

    @NotBlank(message = "Row label is required")
    private String rowLabel;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    private SeatType seatType;

    private SeatStatus status;

    @NotNull(message = "Column position is required")
    private Integer col;

    @NotNull(message = "Row position is required")
    private Integer row;

    private Boolean isActive;
}

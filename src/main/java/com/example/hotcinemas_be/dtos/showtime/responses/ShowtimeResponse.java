package com.example.hotcinemas_be.dtos.showtime.responses;

import com.example.hotcinemas_be.enums.ShowTimeStatus;
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
    private Long id;
    private Long movieId;
    private String movieTitle;
    private Long roomId;
    private String roomName;
    private String startTime;
    private String endTime;
    private BigDecimal ticketPrice;
    private ShowTimeStatus status;
    private Boolean isActive ;
}

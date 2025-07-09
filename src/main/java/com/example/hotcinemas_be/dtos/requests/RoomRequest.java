package com.example.hotcinemas_be.dtos.requests;

import com.example.hotcinemas_be.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequest {
    private String roomNumber;
    private RoomType roomType;
    private Integer rowsCount;
    private Integer seatsPerRow;
    private double priceMultiplier;
}

package com.example.hotcinemas_be.dtos.room.requests;

import com.example.hotcinemas_be.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequest {
    private String name;
    private RoomType roomType;
    private Integer rowsCount;
    private Integer seatsPerRow;
    private BigDecimal price;
    private Boolean isActive;
}


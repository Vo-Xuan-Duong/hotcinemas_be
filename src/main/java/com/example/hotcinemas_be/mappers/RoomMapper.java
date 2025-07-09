package com.example.hotcinemas_be.mappers;

import com.example.hotcinemas_be.dtos.responses.RoomResponse;
import com.example.hotcinemas_be.models.Room;
import org.springframework.stereotype.Service;

@Service
public class RoomMapper {
    public RoomResponse mapToResponse(Room room) {
        if (room == null) {
            return null;
        }

        return RoomResponse.builder()
                .roomId(room.getRoomId())
                .roomNumber(room.getRoomNumber())
                .capacity(room.getCapacity())
                .roomType(room.getRoomType().name()) // Assuming roomType is an enum
                .build();
    }
}

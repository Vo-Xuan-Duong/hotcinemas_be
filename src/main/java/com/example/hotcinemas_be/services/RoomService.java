package com.example.hotcinemas_be.services;

import com.example.hotcinemas_be.dtos.room.requests.RoomRequest;
import com.example.hotcinemas_be.dtos.room.responses.RoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomService {
    public RoomResponse createRoom(Long cinemaId, RoomRequest roomRequest);

    public RoomResponse getRoomById(Long roomId);

    public RoomResponse updateRoom(Long roomId, RoomRequest roomRequest);

    public void deleteRoom(Long roomId);

    public Page<RoomResponse> getPageRooms(Pageable pageable);

    public List<RoomResponse> getAllRooms();

    public Page<RoomResponse> getPageRoomsByCinemaId(Long cinemaId, Pageable pageable);

    public List<RoomResponse> getAllRoomsByCinemaId(Long cinemaId);

    public void deleteRoomsByCinemaId(Long cinemaId);
}

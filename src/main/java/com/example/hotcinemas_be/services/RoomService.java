package com.example.hotcinemas_be.services;


import com.example.hotcinemas_be.dtos.requests.RoomRequest;
import com.example.hotcinemas_be.dtos.responses.RoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomService {
    public RoomResponse createRoom(Long cinemaId, RoomRequest roomRequest);
    public RoomResponse getRoomById(Long roomId);
    public RoomResponse updateRoom(Long roomId, RoomRequest roomRequest);
    public void deleteRoom(Long roomId);
    public Page<RoomResponse> getAllRooms(Pageable pageable);
    public Page<RoomResponse> getRoomsByCinemaId(Long cinemaId, Pageable pageable);
    public void deleteRoomsByCinemaId(Long cinemaId);
}

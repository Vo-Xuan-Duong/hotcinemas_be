package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.dtos.requests.RoomRequest;
import com.example.hotcinemas_be.dtos.responses.RoomResponse;
import com.example.hotcinemas_be.enums.RoomType;
import com.example.hotcinemas_be.exceptions.ErrorCode;
import com.example.hotcinemas_be.exceptions.ErrorException;
import com.example.hotcinemas_be.mappers.RoomMapper;
import com.example.hotcinemas_be.models.Cinema;
import com.example.hotcinemas_be.models.Room;
import com.example.hotcinemas_be.repositorys.CinemaRepository;
import com.example.hotcinemas_be.repositorys.RoomRepository;
import com.example.hotcinemas_be.services.RoomService;
import com.example.hotcinemas_be.services.SeatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final CinemaRepository cinemaRepository;
    private final SeatService seatService;

    public RoomServiceImpl(RoomRepository roomRepository,
                           RoomMapper roomMapper,
                           CinemaRepository cinemaRepository,
                           SeatService seatService) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.cinemaRepository = cinemaRepository;
        this.seatService = seatService;
    }

    @Override
    public RoomResponse createRoom(Long cinemaId ,RoomRequest roomRequest) {
        Cinema cinema = cinemaRepository.findCinemaByCinemaId(cinemaId)
                .orElseThrow(() -> new RuntimeException("Cinema not found with id: " + cinemaId));

        Room room = new Room();
        room.setRoomNumber(roomRequest.getRoomNumber());
        room.setRoomType(roomRequest.getRoomType());
        room.setCapacity(roomRequest.getRowsCount()* roomRequest.getSeatsPerRow());
        room.setCinema(cinema);
        Room savedRoom = roomRepository.save(room);

        seatService.createSeatsForRoom(savedRoom, roomRequest.getRowsCount(), roomRequest.getSeatsPerRow(), roomRequest.getPriceMultiplier());
        return roomMapper.mapToResponse(savedRoom);
    }

    @Override
    public RoomResponse getRoomById(Long roomId) {
        Room room  = roomRepository.findById(roomId)
                .orElseThrow(() -> new ErrorException("Room not found with id: " + roomId, ErrorCode.ERROR_MODEL_NOT_FOUND));
        return roomMapper.mapToResponse(room);
    }

    @Override
    public RoomResponse updateRoom(Long roomId, RoomRequest roomRequest) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ErrorException("Room not found with id: " + roomId, ErrorCode.ERROR_MODEL_NOT_FOUND));

        room.setRoomNumber(roomRequest.getRoomNumber());
        room.setRoomType(roomRequest.getRoomType());
        room.setCapacity(roomRequest.getRowsCount() * roomRequest.getSeatsPerRow());

        return roomMapper.mapToResponse(roomRepository.save(room));
    }

    @Override
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ErrorException("Room not found with id: " + roomId, ErrorCode.ERROR_MODEL_NOT_FOUND));
        roomRepository.delete(room);
    }

    @Override
    public Page<RoomResponse> getAllRooms(Pageable pageable) {
        Page<Room> rooms = roomRepository.findAll(pageable);
        return rooms.map(roomMapper::mapToResponse);
    }

    @Override
    public Page<RoomResponse> getRoomsByCinemaId(Long cinemaId, Pageable pageable) {
        Page<Room> rooms = roomRepository.findRoomsByCinema_CinemaId(cinemaId, pageable);
        if (rooms.isEmpty()) {
            throw new ErrorException("No rooms found for cinema with id: " + cinemaId, ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        return rooms.map(roomMapper::mapToResponse);
    }

    @Override
    public void deleteRoomsByCinemaId(Long cinemaId) {
        List<Room> rooms = roomRepository.findRoomsByCinema_CinemaId(cinemaId, Pageable.unpaged()).getContent();
        if (rooms.isEmpty()) {
            throw new ErrorException("No rooms found for cinema with id: " + cinemaId, ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        for (Room room : rooms) {
            seatService.deleteSeatsByRoomId(room.getRoomId());
            roomRepository.delete(room);
        }
    }

}

package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.dtos.requests.SeatRequest;
import com.example.hotcinemas_be.dtos.responses.SeatResponse;
import com.example.hotcinemas_be.exceptions.ErrorCode;
import com.example.hotcinemas_be.exceptions.ErrorException;
import com.example.hotcinemas_be.mappers.SeatMapper;
import com.example.hotcinemas_be.models.Room;
import com.example.hotcinemas_be.models.Seat;
import com.example.hotcinemas_be.repositorys.RoomRepository;
import com.example.hotcinemas_be.repositorys.SeatRepository;
import com.example.hotcinemas_be.services.SeatService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;
    private final RoomRepository roomRepository;

    public SeatServiceImpl(SeatRepository seatRepository,
                           SeatMapper seatMapper,
                           RoomRepository roomRepository) {
        this.seatRepository = seatRepository;
        this.seatMapper = seatMapper;
        this.roomRepository = roomRepository;
    }

    @Override
    public void createSeatsForRoom(Room room, Integer rowsCount, Integer seatsPerRow, Double priceMultiplier) {
        if (room == null || rowsCount <= 0 || seatsPerRow <= 0) {
            throw new IllegalArgumentException("Invalid room or seat configuration");
        }

        Set<Seat> seats = new HashSet<>();
        Character rowLetter = 'A';

        for (int row = 0; row < rowsCount; row++) {
            String rowNumber = String.valueOf(rowLetter);
            for (int seatNumber = 1; seatNumber <= seatsPerRow; seatNumber++) {
                Seat seat = Seat.builder()
                        .rowNumber(rowNumber)
                        .seatNumber(seatNumber)
                        .isPhysicalAvailable(true) // Assuming all seats are initially available
                        .priceMultiplier(BigDecimal.valueOf(priceMultiplier))
                        .room(room)
                        .build();
                seats.add(seat);
            }
            rowLetter++;
        }

        seatRepository.saveAll(seats);
    }

    @Override
    public void deleteSeatsByRoomId(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ErrorException("Room not found with id: " + roomId, ErrorCode.ERROR_MODEL_NOT_FOUND));
        Set<Seat> seats = room.getSeats();
        if (seats == null || seats.isEmpty()) {
            throw new ErrorException("No seats found for room with id: " + roomId, ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        seatRepository.deleteAll(seats);
    }

    @Override
    public void deleteSeatById(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ErrorException("Seat not found with id: " + seatId, ErrorCode.ERROR_MODEL_NOT_FOUND));
        seatRepository.delete(seat);
    }

    @Override
    public SeatResponse addSeatToRoom(Long roomId, SeatRequest seatRequest) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ErrorException("Room not found with id: " + roomId, ErrorCode.ERROR_MODEL_NOT_FOUND));
        BigDecimal priceMultiplier = room.getSeats().stream()
                .map(Seat::getPriceMultiplier)
                .findFirst()
                .orElse(BigDecimal.ZERO);
        Seat seat = Seat.builder()
                .rowNumber(seatRequest.getRowNumber())
                .seatNumber(seatRequest.getSeatNumber())
                .isPhysicalAvailable(true)
                .priceMultiplier(priceMultiplier)
                .room(room)
                .build();
        return  seatMapper.mapToResponse(seatRepository.save(seat));
    }

    @Override
    public SeatResponse updateSeat(Long seatId, SeatRequest seatRequest) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ErrorException("Seat not found with id: " + seatId, ErrorCode.ERROR_MODEL_NOT_FOUND));
        if (seatRequest.getRowNumber() != null) {
            seat.setRowNumber(seatRequest.getRowNumber());
        }
        if (seatRequest.getSeatNumber() != null) {
            seat.setSeatNumber(seatRequest.getSeatNumber());
        }
        if(seatRequest.getSeatType() != null) {
            seat.setSeatType(seatRequest.getSeatType());
        }
        return  seatMapper.mapToResponse(seatRepository.save(seat));
    }

    @Override
    public List<SeatResponse> getSeatsByRoomId(Long roomId) {
        List<Seat> seats = seatRepository.findAllByRoom_RoomId(roomId);
        if (seats.isEmpty()) {
            throw new ErrorException("No seats found for room with id: " + roomId, ErrorCode.ERROR_MODEL_NOT_FOUND);
        }
        return seats.stream()
                .map(seatMapper::mapToResponse)
                .toList();
    }
}

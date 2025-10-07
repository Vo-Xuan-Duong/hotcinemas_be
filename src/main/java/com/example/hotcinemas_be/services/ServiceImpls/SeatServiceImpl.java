package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.dtos.seat.requests.SeatRequest;
import com.example.hotcinemas_be.dtos.seat.responses.SeatResponse;
import com.example.hotcinemas_be.enums.SeatStatus;
import com.example.hotcinemas_be.enums.SeatType;
import com.example.hotcinemas_be.mappers.SeatMapper;
import com.example.hotcinemas_be.models.Room;
import com.example.hotcinemas_be.models.Seat;
import com.example.hotcinemas_be.repositorys.RoomRepository;
import com.example.hotcinemas_be.repositorys.SeatRepository;
import com.example.hotcinemas_be.services.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;
    private final RoomRepository roomRepository;

    @Override
    public SeatResponse createSeat(SeatRequest seatRequest) {
        // Validate room exists
        Room room = roomRepository.findById(seatRequest.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + seatRequest.getRoomId()));

        // Check if seat already exists at this position
        seatRepository.findByRoomIdAndRowLabelAndSeatNumber(
                seatRequest.getRoomId(),
                seatRequest.getRowLabel(),
                seatRequest.getSeatNumber()).ifPresent(seat -> {
                    throw new RuntimeException("Seat already exists at position " +
                            seatRequest.getRowLabel() + seatRequest.getSeatNumber() + " in room "
                            + seatRequest.getRoomId());
                });

        Seat seat = Seat.builder()
                .room(room)
                .rowLabel(seatRequest.getRowLabel())
                .seatNumber(seatRequest.getSeatNumber())
                .seatType(seatRequest.getSeatType() != null ? seatRequest.getSeatType() : SeatType.NORMAL)
                .status(SeatStatus.AVAILABLE)
                .col(seatRequest.getCol())
                .row(seatRequest.getRow())
                .isActive(seatRequest.getIsActive() != null ? seatRequest.getIsActive() : true)
                .build();

        Seat savedSeat = seatRepository.save(seat);
        return seatMapper.mapToResponse(savedSeat);
    }

    @Override
    @Transactional(readOnly = true)
    public SeatResponse getSeatById(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + seatId));
        return seatMapper.mapToResponse(seat);
    }

    @Override
    public SeatResponse updateSeat(Long seatId, SeatRequest seatRequest) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + seatId));

        // Update other fields
        if (seatRequest.getRowLabel() != null) {
            seat.setRowLabel(seatRequest.getRowLabel());
        }
        if (seatRequest.getSeatNumber() != null) {
            seat.setSeatNumber(seatRequest.getSeatNumber());
        }
        if (seatRequest.getSeatType() != null) {
            seat.setSeatType(seatRequest.getSeatType());
        }
        if (seatRequest.getStatus() != null) {
            seat.setStatus(seatRequest.getStatus());
        }
        if (seatRequest.getCol() != null) {
            seat.setCol(seatRequest.getCol());
        }
        if (seatRequest.getRow() != null) {
            seat.setRow(seatRequest.getRow());
        }
        if (seatRequest.getIsActive() != null) {
            seat.setIsActive(seatRequest.getIsActive());
        }

        Seat updatedSeat = seatRepository.save(seat);
        return seatMapper.mapToResponse(updatedSeat);
    }

    @Override
    public void deleteSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + seatId));
        seatRepository.delete(seat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatResponse> getSeatsByRoomId(Long roomId) {
        List<Seat> seats = seatRepository.findByRoomId(roomId);
        return seats.stream().map(seatMapper::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatResponse> getSeatsByRoomIdAndActive(Long roomId) {
        List<Seat> seats = seatRepository.findByRoomIdAndIsActiveTrue(roomId);
        return seats.stream().map(seatMapper::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatResponse> getSeatsBySeatType(SeatType seatType) {
        List<Seat> seats = seatRepository.findBySeatType(seatType);
        return seats.stream().map(seatMapper::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatResponse> getSeatsByRoomIdAndSeatType(Long roomId, SeatType seatType) {
        List<Seat> seats = seatRepository.findByRoomIdAndSeatType(roomId, seatType);
        return seats.stream().map(seatMapper::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatResponse> getSeatsByCinemaId(Long cinemaId) {
        List<Seat> seats = seatRepository.findByCinemaId(cinemaId);
        return seats.stream().map(seatMapper::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SeatResponse getSeatByRoomAndPosition(Long roomId, String rowLabel, String seatNumber) {
        Seat seat = seatRepository.findByRoomIdAndRowLabelAndSeatNumber(roomId, rowLabel, seatNumber)
                .orElseThrow(() -> new RuntimeException(
                        "Seat not found at position " + rowLabel + seatNumber + " in room " + roomId));
        return seatMapper.mapToResponse(seat);
    }

    @Override
    @Async
    @Transactional
    public void createSeatsForRoom(Long roomId, Integer rowsCount, Integer seatsPerRow) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));

        if (rowsCount <= 0 || seatsPerRow <= 0) {
            throw new IllegalArgumentException("Invalid seat configuration");
        }

        Character rowLetter = 'A';
        for (int row = 0; row < rowsCount; row++) {
            String rowLabel = String.valueOf(rowLetter);
            for (int seatNumber = 1; seatNumber <= seatsPerRow; seatNumber++) {
                Seat seat = Seat.builder()
                        .room(room)
                        .rowLabel(rowLabel)
                        .seatNumber(String.valueOf(seatNumber))
                        .seatType(SeatType.NORMAL)
                        .status(SeatStatus.AVAILABLE)
                        .col(seatNumber)
                        .row(row + 1)
                        .isActive(true)
                        .build();
                seatRepository.save(seat);
            }
            rowLetter++;
        }
    }

    @Override
    public void deleteSeatsByRoomId(Long roomId) {
        List<Seat> seats = seatRepository.findByRoomId(roomId);
        if (seats.isEmpty()) {
            throw new RuntimeException("No seats found for room with id: " + roomId);
        }
        seatRepository.deleteAll(seats);
    }

    @Override
    public SeatResponse activateSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + seatId));
        seat.setIsActive(true);
        Seat updatedSeat = seatRepository.save(seat);
        return seatMapper.mapToResponse(updatedSeat);
    }

    @Override
    public SeatResponse deactivateSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + seatId));
        seat.setIsActive(false);
        Seat updatedSeat = seatRepository.save(seat);
        return seatMapper.mapToResponse(updatedSeat);
    }
}

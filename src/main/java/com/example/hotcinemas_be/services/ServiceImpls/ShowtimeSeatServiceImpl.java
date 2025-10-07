package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.dtos.showtime_seat.responses.ShowtimeSeatResponse;
import com.example.hotcinemas_be.enums.SeatStatus;
import com.example.hotcinemas_be.exceptions.ErrorCode;
import com.example.hotcinemas_be.exceptions.ErrorException;
import com.example.hotcinemas_be.mappers.ShowtimeSeatMapper;
import com.example.hotcinemas_be.models.Room;
import com.example.hotcinemas_be.models.Seat;
import com.example.hotcinemas_be.models.Showtime;
import com.example.hotcinemas_be.models.ShowtimeSeat;
import com.example.hotcinemas_be.repositorys.ShowtimeRepository;
import com.example.hotcinemas_be.repositorys.ShowtimeSeatRepository;
import com.example.hotcinemas_be.repositorys.UserRepository;
import com.example.hotcinemas_be.services.ShowtimeSeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ShowtimeSeatServiceImpl implements ShowtimeSeatService {
    @Value("${seat.hold.minutes:10}")
    private int seatHoldMinutes;

    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final ShowtimeRepository showtimeRepository;
    private final UserRepository userRepository;
    private final ShowtimeSeatMapper showtimeSeatMapper;

    public ShowtimeSeatServiceImpl(ShowtimeSeatRepository showtimeSeatRepository,
            ShowtimeRepository showtimeRepository,
            UserRepository userRepository,
            ShowtimeSeatMapper showtimeSeatMapper) {
        this.showtimeSeatRepository = showtimeSeatRepository;
        this.showtimeRepository = showtimeRepository;
        this.userRepository = userRepository;
        this.showtimeSeatMapper = showtimeSeatMapper;
    }

    @Override
    public void createShowtimeSeats(Long ShowtimeId) {
        Showtime showtime = showtimeRepository.findById(ShowtimeId)
                .orElseThrow(() -> new ErrorException("Showtime not found with id: " + ShowtimeId,
                        ErrorCode.ERROR_MODEL_NOT_FOUND));
        Room room = showtime.getRoom();
        Set<Seat> seats = room.getSeats();

        seats.forEach(seat -> {
            ShowtimeSeat showtimeSeat = new ShowtimeSeat();
            showtimeSeat.setShowtime(showtime);
            showtimeSeat.setSeat(seat);
            showtimeSeat.setStatus(SeatStatus.AVAILABLE); // Default status
            // Set initial price from showtime ticket price (can be adjusted per seat type
            // later)
            showtimeSeat.setPrice(showtime.getTicketPrice());
            showtimeSeatRepository.save(showtimeSeat);
        });
        log.info("Created showtime seats for showtime with id: {}", ShowtimeId);
    }

    @Override
    public void deleteShowtimeSeats(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ErrorException("Showtime not found with id: " + showtimeId,
                        ErrorCode.ERROR_MODEL_NOT_FOUND));

        List<ShowtimeSeat> showtimeSeats = showtimeSeatRepository.findByShowtime(showtime);
        if (showtimeSeats.isEmpty()) {
            log.warn("No showtime seats found for showtime with id: {}", showtimeId);
            return;
        }

        showtimeSeatRepository.deleteAll(showtimeSeats);
        log.info("Deleted all showtime seats for showtime with id: {}", showtimeId);
    }

    @Override
    @Transactional
    public boolean updateShowtimeSeatHeld(Long showtimeSeatId, String status, Long userId, LocalDateTime heldUntil) {
        ShowtimeSeat showtimeSeat = showtimeSeatRepository.findByIdForUpdate(showtimeSeatId)
                .orElseThrow(() -> new ErrorException("Showtime seat not found with id: " + showtimeSeatId,
                        ErrorCode.ERROR_MODEL_NOT_FOUND));
        LocalDateTime now = LocalDateTime.now();
        boolean isExpiredHeld = showtimeSeat.getStatus() == SeatStatus.HELD && showtimeSeat.getHeldUntil() != null
                && showtimeSeat.getHeldUntil().isBefore(now);
        if (!(showtimeSeat.getStatus() == SeatStatus.AVAILABLE || isExpiredHeld)) {
            log.warn("Showtime seat with id: {} is not available for holding", showtimeSeatId);
            return false;
        }

        showtimeSeat.setStatus(SeatStatus.HELD);
        showtimeSeat.setHeldByUser(userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException("User not found", ErrorCode.ERROR_MODEL_NOT_FOUND))); // Uncomment
        // Server-controlled hold time window (ignore client-provided heldUntil)
        showtimeSeat.setHeldUntil(now.plusMinutes(seatHoldMinutes));

        showtimeSeatRepository.save(showtimeSeat);
        log.info("Updated showtime seat with id: {} to HELD status", showtimeSeatId);
        return true;
    }

    @Override
    @Transactional
    public boolean updateShowtimeSeatStatus(Long showtimeSeatId, SeatStatus status, Long userId) {
        ShowtimeSeat showtimeSeat = showtimeSeatRepository.findByIdForUpdate(showtimeSeatId)
                .orElseThrow(() -> new ErrorException("Showtime seat not found with id: " + showtimeSeatId,
                        ErrorCode.ERROR_MODEL_NOT_FOUND));
        LocalDateTime now = LocalDateTime.now();
        // Enforce valid transitions: BOOKED only from HELD and not expired
        if (status == SeatStatus.BOOKED) {
            if (showtimeSeat.getStatus() != SeatStatus.HELD) {
                throw new ErrorException("Seat must be HELD before booking", ErrorCode.ERROR_INVALID_REQUEST);
            }
            if (showtimeSeat.getHeldUntil() == null || showtimeSeat.getHeldUntil().isBefore(now)) {
                throw new ErrorException("Hold expired", ErrorCode.ERROR_INVALID_REQUEST);
            }
            if (showtimeSeat.getHeldByUser() == null || !showtimeSeat.getHeldByUser().getId().equals(userId)) {
                throw new ErrorException("Only holding user can book this seat", ErrorCode.ERROR_INVALID_REQUEST);
            }
        }

        showtimeSeat.setStatus(status);
        if (status == SeatStatus.AVAILABLE) {
            showtimeSeat.setHeldByUser(null);
            showtimeSeat.setHeldUntil(null);
        }
        showtimeSeatRepository.save(showtimeSeat);

        log.info("Updated showtime seat with id: {} to status: {}", showtimeSeatId, status);

        return true;
    }

    @Override
    public List<ShowtimeSeatResponse> getShowtimeSeatsByShowtimeId(Long showtimeId) {
        List<ShowtimeSeat> showtimeSeats = showtimeSeatRepository.findAllByShowtime_Id(showtimeId);
        if (showtimeSeats.isEmpty()) {
            log.warn("No showtime seats found for showtime with id: {}", showtimeId);
            return List.of(); // Return empty list if no seats found
        }
        return showtimeSeats.stream().map(showtimeSeatMapper::mapToResponse).toList();
    }

    // Periodically release expired holds (every 30 seconds)
    @Transactional
    @Scheduled(fixedDelay = 30000)
    public void releaseExpiredHoldsJob() {
        int released = showtimeSeatRepository.releaseExpiredHolds(LocalDateTime.now());
        if (released > 0) {
            log.info("Released {} expired held seats", released);
        }
    }

    @Override
    @Transactional
    public void changeStatusSeatByStaff(Long seatId, SeatStatus status) {
        ShowtimeSeat showtimeSeat = showtimeSeatRepository.findByIdForUpdate(seatId)
                .orElseThrow(() -> new ErrorException("Showtime seat not found with id: " + seatId,
                        ErrorCode.ERROR_MODEL_NOT_FOUND));
        showtimeSeat.setStatus(status);
        if (status == SeatStatus.AVAILABLE) {
            showtimeSeat.setHeldByUser(null);
            showtimeSeat.setHeldUntil(null);
        }
        showtimeSeatRepository.save(showtimeSeat);
        log.info("Staff changed showtime seat id: {} to status: {}", seatId, status);
    }
}

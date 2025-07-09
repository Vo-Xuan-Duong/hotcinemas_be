package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.enums.SeatStatus;
import com.example.hotcinemas_be.exceptions.ErrorCode;
import com.example.hotcinemas_be.exceptions.ErrorException;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ShowtimeSeatServiceImpl implements ShowtimeSeatService {

    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final ShowtimeRepository showtimeRepository;
    private final UserRepository userRepository;

    public ShowtimeSeatServiceImpl(ShowtimeSeatRepository showtimeSeatRepository,
                                   ShowtimeRepository showtimeRepository,
                                   UserRepository userRepository) {
        this.showtimeSeatRepository = showtimeSeatRepository;
        this.showtimeRepository = showtimeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createShowtimeSeats(Long ShowtimeId) {
        Showtime showtime = showtimeRepository.findById(ShowtimeId)
                .orElseThrow(() -> new ErrorException("Showtime not found with id: " + ShowtimeId, ErrorCode.ERROR_MODEL_NOT_FOUND));
        Room room = showtime.getRoom();
        Set<Seat> seats = room.getSeats();

        seats.forEach(seat -> {
            ShowtimeSeat showtimeSeat = new ShowtimeSeat();
            showtimeSeat.setShowtime(showtime);
            showtimeSeat.setSeat(seat);
            showtimeSeat.setStatus(SeatStatus.AVAILABLE); // Default status
            showtimeSeatRepository.save(showtimeSeat);
        });
        log.info("Created showtime seats for showtime with id: {}", ShowtimeId);
    }

    @Override
    public void deleteShowtimeSeats(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ErrorException("Showtime not found with id: " + showtimeId, ErrorCode.ERROR_MODEL_NOT_FOUND));

        List<ShowtimeSeat> showtimeSeats = showtimeSeatRepository.findByShowtime(showtime);
        if (showtimeSeats.isEmpty()) {
            log.warn("No showtime seats found for showtime with id: {}", showtimeId);
            return;
        }

        showtimeSeatRepository.deleteAll(showtimeSeats);
        log.info("Deleted all showtime seats for showtime with id: {}", showtimeId);
    }

    @Override
    public boolean updateShowtimeSeatHeld(Long showtimeSeatId, String status, Long userId, LocalDateTime heldUntil) {
        ShowtimeSeat showtimeSeat = showtimeSeatRepository.findById(showtimeSeatId)
                .orElseThrow(() -> new ErrorException("Showtime seat not found with id: " + showtimeSeatId, ErrorCode.ERROR_MODEL_NOT_FOUND));

        if (showtimeSeat.getStatus() != SeatStatus.AVAILABLE) {
            log.warn("Showtime seat with id: {} is not available for holding", showtimeSeatId);
            return false;
        }

        showtimeSeat.setStatus(SeatStatus.HELD);
        // Assuming heldByUser and heldUntil are set correctly
        showtimeSeat.setHeldByUser(userRepository.findById(userId).orElseThrow(() -> new ErrorException("User not found", ErrorCode.ERROR_MODEL_NOT_FOUND))); // Uncomment when user service is available
        showtimeSeat.setHeldUntil(heldUntil); // Uncomment when date parsing is handled

        showtimeSeatRepository.save(showtimeSeat);
        log.info("Updated showtime seat with id: {} to HELD status", showtimeSeatId);
        return true;
    }

    @Override
    public boolean updateShowtimeSeatStatus(Long showtimeSeatId, SeatStatus status) {
        ShowtimeSeat showtimeSeat = showtimeSeatRepository.findById(showtimeSeatId)
                .orElseThrow(() -> new ErrorException("Showtime seat not found with id: " + showtimeSeatId, ErrorCode.ERROR_MODEL_NOT_FOUND));

        showtimeSeat.setStatus(status);
        showtimeSeatRepository.save(showtimeSeat);

        log.info("Updated showtime seat with id: {} to status: {}", showtimeSeatId, status);

        return true;
    }
}

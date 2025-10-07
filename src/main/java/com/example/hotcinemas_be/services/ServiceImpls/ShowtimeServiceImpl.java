package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.dtos.showtime.requests.ShowtimeRequest;
import com.example.hotcinemas_be.dtos.showtime.responses.ShowtimeResponse;
import com.example.hotcinemas_be.exceptions.ErrorCode;
import com.example.hotcinemas_be.exceptions.ErrorException;
import com.example.hotcinemas_be.mappers.ShowtimeMapper;
import com.example.hotcinemas_be.models.Movie;
import com.example.hotcinemas_be.models.Room;
import com.example.hotcinemas_be.models.Showtime;
import com.example.hotcinemas_be.repositorys.MovieRepository;
import com.example.hotcinemas_be.repositorys.RoomRepository;
import com.example.hotcinemas_be.repositorys.ShowtimeRepository;
import com.example.hotcinemas_be.services.ShowtimeSeatService;
import com.example.hotcinemas_be.services.ShowtimeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final ShowtimeMapper showtimeMapper;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;
    private final ShowtimeSeatService showtimeSeatService;

    public ShowtimeServiceImpl(ShowtimeRepository showtimeRepository,
            ShowtimeMapper showtimeMapper,
            RoomRepository roomRepository,
            MovieRepository movieRepository,
            ShowtimeSeatService showtimeSeatService) {
        this.showtimeRepository = showtimeRepository;
        this.showtimeMapper = showtimeMapper;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.showtimeSeatService = showtimeSeatService;
    }

    @Override
    public ShowtimeResponse createShowtime(ShowtimeRequest showtimeRequest) {
        Room room = roomRepository.findById(showtimeRequest.getRoomId()).orElseThrow(
                () -> new ErrorException("Room not found with id: " + showtimeRequest.getRoomId(),
                        ErrorCode.ERROR_MODEL_NOT_FOUND));

        Movie movie = movieRepository.findById(showtimeRequest.getMovieId())
                .orElseThrow(() -> new ErrorException("Movie not found with id: " + showtimeRequest.getMovieId(),
                        ErrorCode.ERROR_MODEL_NOT_FOUND));

        if(isOverlappingShowtime(showtimeRequest.getRoomId(), showtimeRequest.getStartTime(),
                showtimeRequest.getStartTime().plusMinutes(movie.getDurationMinutes()))) {
            throw new ErrorException("Showtime overlaps with an existing showtime in the same room.",
                    ErrorCode.ERROR_SHOWTIME_CONFLICT);
        }

        Showtime showtime = new Showtime();
        showtime.setRoom(room);
        showtime.setStartTime(showtimeRequest.getStartTime());
        showtime.setEndTime(showtimeRequest.getStartTime().plusMinutes(movie.getDurationMinutes()));
        showtime.setTicketPrice(BigDecimal.valueOf(showtimeRequest.getTicketPrice()));
        showtime.setMovie(movie);
        showtime.setIsActive(true); // Default to active
        showtime = showtimeRepository.save(showtime);

        // Create seats for the showtime
        showtimeSeatService.createShowtimeSeats(showtime.getId());

        return showtimeMapper.mapToResponse(showtime);
    }

    private Boolean isOverlappingShowtime(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        // Overlap if any existing.startTime < newEnd AND existing.endTime > newStart
        return showtimeRepository.existsByRoom_IdAndStartTimeLessThanAndEndTimeGreaterThan(roomId, endTime, startTime);
    }

    @Override
    public ShowtimeResponse updateShowtime(Long showtimeId, ShowtimeRequest showtimeRequest) {
        Room room = roomRepository.findById(showtimeRequest.getRoomId()).orElseThrow(
                () -> new ErrorException("Room not found with id: " + showtimeRequest.getRoomId(),
                        ErrorCode.ERROR_MODEL_NOT_FOUND));
        Movie movie = movieRepository.findById(showtimeRequest.getMovieId())
                .orElseThrow(() -> new ErrorException("Movie not found with id: " + showtimeRequest.getMovieId(),
                        ErrorCode.ERROR_MODEL_NOT_FOUND));

        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ErrorException("Showtime not found with id: " + showtimeId,
                        ErrorCode.ERROR_MODEL_NOT_FOUND));
        showtime.setRoom(room);
        showtime.setStartTime(showtimeRequest.getStartTime());
        showtime.setEndTime(showtimeRequest.getStartTime().plusMinutes(movie.getDurationMinutes()));
        showtime.setTicketPrice(BigDecimal.valueOf(showtimeRequest.getTicketPrice()));
        showtime.setMovie(movie);
        showtime.setIsActive(showtimeRequest.getIsActive()); // Default to active
        showtime = showtimeRepository.save(showtime);

        return showtimeMapper.mapToResponse(showtime);
    }

    @Override
    public ShowtimeResponse getShowtimeById(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ErrorException("Showtime not found with id: " + showtimeId,
                        ErrorCode.ERROR_MODEL_NOT_FOUND));
        return showtimeMapper.mapToResponse(showtime);
    }

    @Override
    public Page<ShowtimeResponse> getAllShowTimes(Pageable pageable) {
        Page<Showtime> showtimePage = showtimeRepository.findAll(pageable);
        return showtimePage.map(showtimeMapper::mapToResponse);
    }

    @Override
    public void deleteShowtime(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ErrorException("Showtime not found with id: " + showtimeId,
                        ErrorCode.ERROR_MODEL_NOT_FOUND));
        // Delete all seats associated with the showtime
        showtimeSeatService.deleteShowtimeSeats(showtimeId);
        showtimeRepository.delete(showtime);
    }

    @Override
    public Page<ShowtimeResponse> getShowtimesByMovieId(Long movieId, Pageable pageable) {
        Page<Showtime> showtimePage = showtimeRepository.findByMovie_Id(movieId, pageable);
        return showtimePage.map(showtimeMapper::mapToResponse);
    }

    @Override
    public Page<ShowtimeResponse> getShowtimesByRoomId(Long roomId, Pageable pageable) {
        Page<Showtime> showtimePage = showtimeRepository.findByRoom_Id(roomId, pageable);
        return showtimePage.map(showtimeMapper::mapToResponse);
    }

    @Override
    public void activateShowtime(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ErrorException("Showtime not found with id: " + showtimeId,
                        ErrorCode.ERROR_MODEL_NOT_FOUND));
        showtime.setIsActive(true);
        showtimeRepository.save(showtime);
    }

    @Override
    public void deactivateShowtime(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ErrorException("Showtime not found with id: " + showtimeId,
                        ErrorCode.ERROR_MODEL_NOT_FOUND));
        showtime.setIsActive(false);
        showtimeRepository.save(showtime);
    }

    @Override
    public void deleteShowtimesByMovieId(Long movieId) {
        List<Showtime> showtimes = showtimeRepository.findByMovie_Id(movieId);
        if (showtimes.isEmpty()) {
            return;
        }
        showtimes.forEach(showtime -> {
            // Delete all seats associated with the showtime
            showtimeSeatService.deleteShowtimeSeats(showtime.getId());
            showtimeRepository.delete(showtime);
        });

    }

    @Override
    public void deleteShowtimesByRoomId(Long roomId) {
        List<Showtime> showtimes = showtimeRepository.findByRoom_Id(roomId);
        if (showtimes.isEmpty()) {
            return;
        }
        showtimes.forEach(showtime -> {
            // Delete all seats associated with the showtime
            showtimeSeatService.deleteShowtimeSeats(showtime.getId());
            showtimeRepository.delete(showtime);
        });
    }
}

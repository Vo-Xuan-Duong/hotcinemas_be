package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.dtos.requests.ShowtimeRequest;
import com.example.hotcinemas_be.dtos.responses.ShowtimeResponse;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
    @Async
    public ShowtimeResponse createShowtime(Long roomId ,ShowtimeRequest showtimeRequest) {
        Room room = roomRepository.findById(roomId).orElseThrow(() ->
            new ErrorException("Room not found with id: " + roomId, ErrorCode.ERROR_MODEL_NOT_FOUND));
        Movie movie = movieRepository.findById(showtimeRequest.getMovieId()).orElseThrow(() ->
            new ErrorException("Movie not found with id: " + showtimeRequest.getMovieId(), ErrorCode.ERROR_MODEL_NOT_FOUND));

        Showtime showtime = new Showtime();
        showtime.setRoom(room);
        showtime.setStartTime(showtimeRequest.getStartTime());
        showtime.setEndTime(showtimeRequest.getStartTime().plusMinutes(movie.getDurationMinutes()));
        showtime.setTicketPrice(BigDecimal.valueOf(showtimeRequest.getTicketPrice()));
        showtime.setMovie(movie);
        showtime.setIsActive(true); // Default to active
        showtime = showtimeRepository.save(showtime);

        // Create seats for the showtime
        showtimeSeatService.createShowtimeSeats(showtime.getShowtimeId());

        return showtimeMapper.mapToResponse(showtime);
    }

    @Override
    public ShowtimeResponse updateShowtime(Long showtimeId, ShowtimeRequest showtimeRequest) {
        return null;
    }

    @Override
    public ShowtimeResponse getShowtimeById(Long showtimeId) {
        return null;
    }

    @Override
    public Page<ShowtimeResponse> getAllShowTimes(Pageable pageable) {
        return null;
    }

    @Override
    public void deleteShowtime(Long showtimeId) {

    }

    @Override
    public Page<ShowtimeResponse> getShowtimesByMovieId(Long movieId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ShowtimeResponse> getShowtimesByRoomId(Long roomId, Pageable pageable) {
        return null;
    }

    @Override
    public void activateShowtime(Long showtimeId) {

    }

    @Override
    public void deactivateShowtime(Long showtimeId) {

    }

    @Override
    public void deleteShowtimesByMovieId(Long movieId) {

    }

    @Override
    public void deleteShowtimesByRoomId(Long roomId) {

    }
}

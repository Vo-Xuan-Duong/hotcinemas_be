package com.example.hotcinemas_be.services;


import com.example.hotcinemas_be.dtos.requests.ShowtimeRequest;
import com.example.hotcinemas_be.dtos.responses.ShowtimeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShowtimeService {
    public ShowtimeResponse createShowtime(Long roomId ,ShowtimeRequest showtimeRequest);
    public ShowtimeResponse updateShowtime(Long showtimeId, ShowtimeRequest showtimeRequest);
    public ShowtimeResponse getShowtimeById(Long showtimeId);
    public Page<ShowtimeResponse> getAllShowTimes(Pageable pageable);
    public void deleteShowtime(Long showtimeId);
    public Page<ShowtimeResponse> getShowtimesByMovieId(Long movieId, Pageable pageable);
    public Page<ShowtimeResponse> getShowtimesByRoomId(Long roomId, Pageable pageable);
    public void activateShowtime(Long showtimeId);
    public void deactivateShowtime(Long showtimeId);
    public void deleteShowtimesByMovieId(Long movieId);
    public void deleteShowtimesByRoomId(Long roomId);

}

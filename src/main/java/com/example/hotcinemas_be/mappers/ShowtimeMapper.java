package com.example.hotcinemas_be.mappers;

import com.example.hotcinemas_be.dtos.showtime.requests.ShowtimeRequest;
import com.example.hotcinemas_be.dtos.showtime.responses.ShowtimeResponse;
import com.example.hotcinemas_be.models.Showtime;
import org.springframework.stereotype.Service;

@Service
public class ShowtimeMapper {
    public ShowtimeResponse mapToResponse(Showtime showtime) {
        if (showtime == null) {
            return null;
        }
        return ShowtimeResponse.builder()
                .id(showtime.getId())
                .movieId(showtime.getMovie().getId())
                .movieTitle(showtime.getMovie().getTitle())
                .roomId(showtime.getRoom().getId())
                .roomName(showtime.getRoom().getName())
                .startTime(showtime.getStartTime().toString())
                .endTime(showtime.getEndTime().toString())
                .ticketPrice(showtime.getTicketPrice())
                .status(showtime.getStatus())
                .isActive(showtime.getIsActive())
                .build();
    }
}

package com.example.hotcinemas_be.mappers;

import com.example.hotcinemas_be.dtos.requests.ShowtimeRequest;
import com.example.hotcinemas_be.dtos.responses.ShowtimeResponse;
import com.example.hotcinemas_be.models.Showtime;
import org.springframework.stereotype.Service;

@Service
public class ShowtimeMapper {
    public ShowtimeResponse mapToResponse(Showtime showtime){
        if(showtime == null) {
            return null;
        }
        return ShowtimeResponse.builder()
                .showtimeId(showtime.getShowtimeId())
                .movieTitle(showtime.getMovie().getTitle())
                .roomName(showtime.getRoom().getRoomNumber())
                .startTime(showtime.getStartTime().toString())
                .endTime(showtime.getEndTime().toString())
                .ticketPrice(showtime.getTicketPrice())
                .isActive(showtime.getIsActive())
                .build();
    }
}

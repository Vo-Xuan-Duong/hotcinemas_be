package com.example.hotcinemas_be.controllers;


import com.example.hotcinemas_be.services.ShowtimeSeatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/showtime-seats")
@Tag(name = "Showtime Seat Management", description = "APIs for managing showtime seats")
public class ShowtimeSeatController {

    private final ShowtimeSeatService showtimeSeatService;

    public ShowtimeSeatController (ShowtimeSeatService showtimeSeatService) {
        this.showtimeSeatService = showtimeSeatService;
    }


}
